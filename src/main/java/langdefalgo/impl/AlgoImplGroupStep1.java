package langdefalgo.impl;

import attrib.types.RUNTIME_ATTRIB;
import err.ERR_TYPE;
import generictree.iface.IGTree;
import langdef.CMD;
import langdef.LangConstants;
import langdef.STRUCT_LIST_TYPE;
import langdefalgo.iface.IAlgoStrategy;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;
import stackpayload.impl.PayloadStateAccStr;
import stackpayload.impl.StackPayload;
import textevent.impl.TextEvent;

import java.util.ArrayList;
import java.util.List;

import static err.ERR_TYPE.*;
import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LIST_TYPE.*;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.*;
import static langdefalgo.strategy.PushPopStrategyGroup.*;

public class AlgoImplGroupStep1 {
    private IAlgoStrategy[] pushes(PushStrategy... pushes){
        return pushes;
    }
    private IAlgoStrategy[] pops(PopStrategy... pops){
        return pops;
    }
    private static final int FIRST = 0, SECOND = 1;

    public void initAlgos(){
        // Struct keyword  // no sharing singletons: each algo needs a unique state
        ATTRIB.initAlgo(
                new RuntimeAttrib(),
                pushes(IDENTIFY),
                pops()
        );
        CONSTANT.initAlgo(
                new CopyOne(),
                pushes(IDENTIFY, MUTE_ROOT),
                pops(UN_IDENTIFY, UN_MUTE_ROOT)
        );
        RX.initAlgo(
                new CopyAll(),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE, UN_IDENTIFY)
        );
        FX.initAlgo(
                new CopyAll(),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE, UN_IDENTIFY)
        );
        FUN.initAlgo(new CopyAll(),
                pushes(IDENTIFY, MUTE_ROOT),
                pops(UN_IDENTIFY, UN_MUTE_ROOT)
        );
        SCOPE.initAlgo(new ScopeStateMachine(),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE, UN_IDENTIFY)
        );
        IF.initAlgo(new ScopeStateMachine(),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE, UN_IDENTIFY)
        );
        ELSE.initAlgo(new ErrOnCoreTask(UNKNOWN_LANG_STRUCT),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE, UN_IDENTIFY)
        );
        CATEGORY.initAlgo(new Category(),
                pushes(IDENTIFY),
                pops()
        );

        // Struct list type
        LIST_STRING.initAlgo(new ListTypeAlgo(), pushes(IDENTIFY, MUTE_ROOT), pops(UN_MUTE_ROOT));
        LIST_NUMBER.initAlgo(new ListTypeAlgo(), pushes(IDENTIFY, MUTE_ROOT), pops(UN_MUTE_ROOT));
        LIST_BOOLEAN.initAlgo(new ListTypeAlgo(), pushes(IDENTIFY, MUTE_ROOT), pops(UN_MUTE_ROOT));
        LIST_DISCRETE.initAlgo(new ListTypeAlgo(), pushes(IDENTIFY, MUTE_ROOT), pops(UN_MUTE_ROOT));
        LIST_VOTE.initAlgo(new ListTypeAlgo(), pushes(IDENTIFY, MUTE_ROOT), pops(UN_MUTE_ROOT));
        LIST_SCOPE.initAlgo(new ListScopeAlgo(), pushes(IDENTIFY, MUTE_ROOT), pops(UN_MUTE_ROOT));

        // Struct lookup
        //ID_DEFINE.initAlgo(new Nop(), null, null);
        ID_ACCESS.initAlgo(new ErrOnCoreTask(DEV_ERROR),
                pushes(PUT_PUSH_NODE, POP_NOW),
                pops()
        );
        //COMMENT.initAlgo(new Nop(), null, null);

        // Struct non-keyword
        LANG_T.initAlgo(new LangT(),
                pushes(),
                pops()
        );
        SCOPE_TEST_ITEM.initAlgo(new CopyAll(),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE)
        );
        //RX_WORD.initAlgo(new Nop(), null, null);
        //FX_WORD.initAlgo(new Nop(), null, null);
        //LANG_ROOT_1.initAlgo(new Nop(), null, null);

        // Struct symbol
        LANG_S.initAlgo(new ErrOnCoreTask(UNKNOWN_LANG_STRUCT),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE)
        );
        LANG_T_INSERT.initAlgo(new LangT(),
                pushes(PUT_PUSH_NODE),
                pops(PUT_POP_NODE)
        );
        SCOPE_TEST.initAlgo(new ScopeTest(),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE)
        );
        CODE_BLOCK.initAlgo(new AlgoCodeBlock(),
                pushes(IDENTIFY),
                pops()
        );
        A_FX.initAlgo(new CopyAll(),
                pushes(IDENTIFY, PUT_PUSH_NODE),
                pops(PUT_POP_NODE, UN_IDENTIFY)
        );
    }

    private static class Nop extends AlgoBase {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            return false;
        }
    }

    // core task copies current read node(s)
    private static class CopyAll extends AlgoBase{

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            if(stackTop.getState().getInt() != SECOND){
                Glob.ERR.kill(ERR_TYPE.NO_ENCLOSING_BRACES);
            }
            Glob.DATA_SINK.put();
            return true;
        }

        @Override
        public void onNest(IStackPayload newTop) {
            IStackPayload oldTop = newTop.getBelow();// old top is this
            if(oldTop.getState().getInt() != FIRST){
                Glob.ERR.kill(NO_ENCLOSING_BRACES);
            }
            oldTop.getState().set(SECOND);
        }
    }

    // err if more than one item included
    private static class CopyOne extends AlgoBase {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            stackTop = stackTop.getSelf();
            if(stackTop.getState().getInt() != FIRST){
                Glob.ERR.kill(ERR_TYPE.CONSTANT_NOT_SINGLE);
            }
            stackTop.getState().set(SECOND);
            Glob.DATA_SINK.put();
            return true;
        }
    }

    // enum only nests other enums; should never reach core task
    private static class ErrOnCoreTask extends AlgoBase {
        private final ERR_TYPE errType;

        public ErrOnCoreTask(ERR_TYPE errType){
            this.errType = errType;
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(errType);
            return false;
        }
    }

    // Nests ScopeTest once
    private static class ScopeStateMachine extends AlgoBase {
        @Override
        public void onPop(IStackPayload stackTop) {
            super.onPop(stackTop);
            if(stackTop.getState().getInt() != SECOND){
                Glob.ERR.kill(ERR_TYPE.MISSING_CONDITIONAL);
            }
        }

        @Override
        public void onNest(IStackPayload newTop) {
            IStackPayload oldTop = newTop.getBelow();// old top is this
            if(newTop.getParentEnum() == SCOPE_TEST){
                if(oldTop.getState().getInt() != FIRST){
                    Glob.ERR.kill(ERR_TYPE.BAD_TEST_PARAM);
                }
                oldTop.getState().set(SECOND);
            }
            else if(oldTop.getState().getInt() != SECOND){
                Glob.ERR.kill(ERR_TYPE.MISSING_CONDITIONAL);
            }
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(ERR_TYPE.BAD_TEST_PARAM);
            return false;
        }
    }

    private static class ScopeTest extends AlgoBase {
        // Nests RX or SCOPE_TEST_ITEM, not both.
        // If RX, core task never called;
        // Else if not RX, copies with add node;

        @Override
        public void onNest(IStackPayload newTop) {
            if(newTop.getParentEnum() != Glob.ENUMS_BY_TYPE.enumCodeBlock()){
                IStackPayload oldTop = newTop.getBelow().getSelf();// old top is this
                if(oldTop.getState().getInt() == FIRST){
                    oldTop.getState().set(SECOND);
                }
                else{
                    Glob.ERR.kill(ERR_TYPE.BAD_TEST_PARAM);
                }
            }
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            if(stackTop.getState().getInt() == FIRST){
                stackTop.getState().set(SECOND);
                Glob.DATA_SINK.put();
                return true;
            }
            else{
                Glob.ERR.kill(ERR_TYPE.BAD_TEST_PARAM);
            }
            return false;
        }
    }

    // de-tokenizes lines for direct copying of target language code
    private static class LangT extends AlgoBase {
        private void onEndLine(IStackPayload stackTop){// reconstruct lines; Scan: no task
            String line = stackTop.getState().getString();                      // accumulated string
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            IReadNode newNode = ReadNode.builder().copy(currNode).text(line).textEvent(null).build();
            Glob.DATA_SINK.put(newNode);

        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode readNode = Glob.RUN_STATE.getCurrNode();
            stackTop.getState().set(readNode.indentedText());                   // accumulate tokenized words
            if(readNode.endLine()){                                             // if end line, reconstruct line
                this.onEndLine(stackTop);
            }
            return true;
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            this.onEndLine(stackTop);                                           // dump accumulated before pop
            super.onPop(stackTop);
        }

        @Override
        public IStackPayload newStackPayload() {
            return new StackPayload(this.parentEnum, new PayloadStateAccStr());
        }
    }

    // routes all nested content to a path tree (Re-entrant process; can separate definitions if desired)
    private static class ListTypeAlgo extends AlgoBase{
        @Override
        public void onPush(IStackPayload stackTop) {
            super.onPush(stackTop);
            STRUCT_LIST_TYPE listType = ((STRUCT_LIST_TYPE)parentEnum);
            listType.getListTree().put(listType.toString());
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            super.onPop(stackTop);
            this.displayTree();
        }

        protected void displayTree(){
            STRUCT_LIST_TYPE parentListType = ((STRUCT_LIST_TYPE)parentEnum);
            IGTree<IReadNode> listTree = parentListType.getListTree();
            System.out.println(parentEnum.toString() + ": LIST TYPE on pop");
            //parentListType.getListSink().getParse().preOrder(listTree.getRoot(), new TaskDispReadNode());
            List<String> paths = listTree.getParse().getAllPaths(listTree.getRoot(), LangConstants.PATH_TREE_SEP);
            for(String path : paths){
                System.out.println(path);
            }
            System.out.println(parentEnum.toString() + ": end display");
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(UNKNOWN_LANG_STRUCT);
            return false;
        }
    }

    // only allows leaves in path tree
    private static class ListScopeAlgo extends ListTypeAlgo {
        private static final String PATH_FORMAT = "%s" + LangConstants.PATH_TREE_SEP + "%s";

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            STRUCT_LIST_TYPE listType = (STRUCT_LIST_TYPE)parentEnum;
            String path = String.format(PATH_FORMAT, listType.toString(), currNode.text());
            listType.getListTree().put(path, currNode);
            return true;
        }
    }

    // with ListTypeAlgo: builds path tree to match given structure
    private static class Category extends AlgoBase{
        private static final String PATH_SEP = String.valueOf(LangConstants.PATH_TREE_SEP);
        private final ArrayList<String> tempPath;
        private STRUCT_LIST_TYPE tempListType;

        public Category() {
            tempPath = new ArrayList<>();
        }
        private void getPathRecurse(IStackPayload stackPayload){
            stackPayload = stackPayload.getBelow().getSelf();

            if(stackPayload.getParentEnum() == CATEGORY){
                getPathRecurse(stackPayload);
                tempPath.add(stackPayload.getState().getString());
                //System.out.println("category: " + stackPayload.getState().getString());
            }
            else{
                tempListType = (STRUCT_LIST_TYPE)stackPayload.getParentEnum();
                //System.out.println("listType: " + tempListType);
                tempPath.add(tempListType.toString());
            }
        }
        private void findPath(IStackPayload stackPayload){
            tempPath.clear();
            getPathRecurse(stackPayload);
            tempPath.add(stackPayload.getState().getString());
        }
        private void findPath(String identifier, IStackPayload stackPayload){
            tempPath.clear();
            getPathRecurse(stackPayload);
            tempPath.add(identifier);
            stackPayload.getState().set(identifier);
        }

        @Override
        public void onPush(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            String identifier;
            if((identifier = currNode.textEvent().substring()) != null){// super.onPush() deletes the substring so get it now
                this.findPath(identifier, stackTop);
                //System.out.println("onPush: categoryPath: " + String.join(PATH_SEP, tempPath));
                tempListType.getListTree().put(String.join(PATH_SEP, tempPath), currNode);
            }
            super.onPush(stackTop);
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            this.findPath(stackTop);
            tempPath.add(currNode.text());
            //System.out.println("coreTask: categoryPath: " + String.join(PATH_SEP, tempPath));
            tempListType.getListTree().put(String.join(PATH_SEP, tempPath), currNode);
            return true;
        }
    }

    // handles run attributes in key=value form; silent push/pop so it does not persist to the next parse step
    private static class RuntimeAttrib extends AlgoBase {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode readNode = Glob.RUN_STATE.getCurrNode();
            Glob.ERR.check(RUNTIME_ATTRIB.props.put(readNode.text()));
            return false;
        }
    }

    // manages pop actions, removed after step 1
    private static class AlgoCodeBlock extends AlgoBase {
//        @Override
//        public IStackPayload newStackPayload() {
//            return new StackPayloadAlias(this.parentEnum);
//        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            stackTop = stackTop.getSelf();
            return stackTop.getParentEnum().doCoreTask(stackTop);
        }
    }
}
