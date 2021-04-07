package langdefalgo.impl;

import attrib.types.RUNTIME_ATTRIB;
import err.ERR_TYPE;
import generictree.iface.IGTree;
import generictree.task.TaskDispReadNode;
import langdef.CMD;
import langdef.LangConstants;
import langdef.STRUCT_LIST_TYPE;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;
import stackpayload.impl.PayloadStateAccStr;
import stackpayload.impl.StackPayload;
import textevent.impl.TextEvent;

import static err.ERR_TYPE.DEV_ERROR;
import static err.ERR_TYPE.UNKNOWN_LANG_STRUCT;
import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LIST_TYPE.*;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.*;

public class AlgoImplGroupStep1 {
    private static final int FIRST = 0, SECOND = 1;

    public void initAlgos(){

        // Struct keyword  // no sharing singletons: each algo needs a unique state
        ATTRIB.initAlgo(        new RuntimeAttrib());
        CONSTANT.initAlgo(      new AltSinkSingleItem());
        RX.initAlgo(            new WordGroup(RX_WORD));
        FX.initAlgo(            new WordGroup(FX_WORD));
        FUN.initAlgo(           new AltSink());
        SCOPE.initAlgo(         new ScopeStateMachine());
        IF.initAlgo(            new ScopeStateMachine());
        ELSE.initAlgo(          new ErrOnCoreTask());
        CATEGORY.initAlgo(      new Category());
        INCLUDE.initAlgo(       new AddTo());

        // Struct list type
        LIST_STRING.initAlgo(   new ListTypeAlgo());
        LIST_NUMBER.initAlgo(   new ListTypeAlgo());
        LIST_BOOLEAN.initAlgo(  new ListTypeAlgo());
        LIST_DISCRETE.initAlgo( new ListTypeAlgo());
        LIST_VOTE.initAlgo(     new ListTypeAlgo());
        LIST_SCOPE.initAlgo(    new ListScopeAlgo());

        // Struct lookup
        ID_DEFINE.initAlgo(     new Nop());
        ID_ACCESS.initAlgo(     new Ignore());
        COMMENT.initAlgo(       new Nop());

        // Struct non-keyword
        LANG_T.initAlgo(        new LangT());
        SCOPE_TEST_ITEM.initAlgo(new AddTo());
        RX_WORD.initAlgo(       new Nop());
        FX_WORD.initAlgo(       new Nop());
        LANG_ROOT_1.initAlgo(   new Nop());

        // Struct symbol
        LANG_S.initAlgo(        new ErrOnCoreTask());
        LANG_T_INSERT.initAlgo( new LangT());
        SCOPE_TEST.initAlgo(    new ScopeTest(SCOPE_TEST_ITEM));
        CODE_BLOCK.initAlgo(    new AlgoCodeBlock());
        A_FX.initAlgo(          new WordGroup(FX_WORD));
    }

    public static class Nop extends AlgoBase {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            return false;
        }
    }

    public static class Ignore extends AlgoBase {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(DEV_ERROR);
            return true;
        }

        @Override
        public void onPush(IStackPayload stackTop) {
            super.onPush(stackTop);
            Glob.RUN_STATE.getStack().pop();
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            //onPop_putPopNode();
        }
    }

    public static class AddTo extends AlgoBase {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            currNode.setTextEvent(new TextEvent(parentEnum, CMD.ADD_TO, currNode.text()));
            Glob.DATA_SINK.put(currNode);
            return true;
        }
    }

    // enum only nests other enums; should never reach core task
    public static class ErrOnCoreTask extends AlgoBase {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(UNKNOWN_LANG_STRUCT);
            return false;
        }
    }

    // RX, FX: adds a word algo to each word; virtual push/pop (to sink, not actually pushed)
    public static class WordGroup extends AlgoBase{
        private final LANG_STRUCT wordStruct;

        public WordGroup(LANG_STRUCT wordStruct) {
            super();
            this.wordStruct = wordStruct;
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {// add push/pop node without pushing to stack
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            //System.out.println("WordGroup core task");
            IReadNode wordPushNode = ReadNode.builder().copy(currNode).textEvent(new TextEvent(wordStruct, CMD.PUSH)).build();
            Glob.DATA_SINK.put(wordPushNode);

            IReadNode addNode = ReadNode.builder().copy(currNode).textEvent(new TextEvent(wordStruct, CMD.ADD_TO, currNode.text())).build();
            Glob.DATA_SINK.put(addNode);

            IReadNode wordPopNode = ReadNode.builder().copy(currNode).textEvent(new TextEvent(wordStruct, CMD.POP)).build();
            Glob.DATA_SINK.put(wordPopNode);
            return false;
        }
    }

    // Pushes and pops a test to handle conditional; then hosts the truthy part
    public static class ScopeStateMachine extends AlgoBase {
        @Override
        public void onPop(IStackPayload stackTop) {
            super.onPop(stackTop);
            if(stackTop.getState().getInt() != SECOND){
                Glob.ERR.kill(ERR_TYPE.MISSING_CONDITIONAL);
            }
        }
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(ERR_TYPE.BAD_TEST_PARAM);
            return false;
        }
    }

    public static class ScopeTest extends AlgoBase {
        // Nests RX or SCOPE_TEST_ITEM, not both.
        // If RX, core task never called;
        // Else if not RX, core task adds SCOPE_TEST_ITEM push/pop to DataSink;
        private final LANG_STRUCT scopeItem;

        public ScopeTest(LANG_STRUCT scopeItem) {
            super();
            this.scopeItem = scopeItem;
        }

        @Override
        public void onPush(IStackPayload stackTop) {
            super.onPush(stackTop);
            this.setStateBelow(stackTop);
        }

        private void setStateBelow(IStackPayload stackTop){// enforce rules
            IStackPayload below = stackTop.getBelow();
            if(below.getState().getInt() != FIRST){     // assert this is first/only ScopeTest pushed
                Glob.ERR.kill(ERR_TYPE.BAD_TEST_PARAM);
            }
            below.getState().set(SECOND);               // notify below this ScopeTest has been pushed
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {// add push/pop node without pushing to stack
            //System.out.println("Scope test doCoreTask start: "+ stackPayload.toString());
            int state = stackTop.getState().getInt();
            if(state == FIRST){
                stackTop.getState().set(SECOND);

                IReadNode currNode = Glob.RUN_STATE.getCurrNode();

                IReadNode itemPushNode = ReadNode.builder().copy(currNode).textEvent(new TextEvent(scopeItem, CMD.PUSH)).build();
                Glob.DATA_SINK.put(itemPushNode);

                IReadNode addNode = ReadNode.builder().copy(currNode).textEvent(new TextEvent(scopeItem, CMD.ADD_TO, currNode.text())).build();
                Glob.DATA_SINK.put(addNode);

                IReadNode itemPopNode = ReadNode.builder().copy(currNode).textEvent(new TextEvent(scopeItem, CMD.POP)).build();
                Glob.DATA_SINK.put(itemPopNode);
                //System.out.println("Scope test doCoreTask finish");
                return true;
            }
            else{
                Glob.ERR.kill(ERR_TYPE.BAD_TEST_PARAM);
            }
            //System.out.println("Scope test doCoreTask skipped");
            return false;
        }
    }

    // de-tokenizes lines for direct copying of target language code
    public static class LangT extends AlgoBase {
        private void onEndLine(IStackPayload stackPayload){// reconstruct lines; Scan: no task
            String line = stackPayload.getState().getString();                      // accumulated string
            Glob.RUN_STATE.getCurrNode().setTextEvent(new TextEvent(parentEnum, CMD.ADD_TO, line));// reconstruct line
            //System.out.println("LangT: doCoreTask: endLine: " + Glob.RUN_STATE.getCurrNode().csvString());
            Glob.DATA_SINK.put();

        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode readNode = Glob.RUN_STATE.getCurrNode();
            stackTop.getState().set(readNode.indentedText());                   // accumulate tokenized words
            if(readNode.endLine()){                                                 // if end line, reconstruct line
                this.onEndLine(stackTop);
            }
            return true;
        }
        @Override
        public void onPush(IStackPayload stackPayload) {}
        @Override
        public void onPop(IStackPayload stackPayload) {
            this.onEndLine(stackPayload);                                           // dump accumulated before pop
            //super.onPop(stackPayload);
        }

        @Override
        public IStackPayload newStackPayload() {
            return new StackPayload(this.parentEnum, new PayloadStateAccStr());
        }
    }

    // routes all nested content to a path tree (Re-entrant process; can separate definitions if desired)
    public static class ListTypeAlgo extends AlgoBase{
        @Override
        public void onPush(IStackPayload stackTop) {
            this.onPush_checkIdentifierRule(stackTop);
            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.enumLangRoot1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(false);

            STRUCT_LIST_TYPE parentListType = ((STRUCT_LIST_TYPE)parentEnum);
            parentListType.getListSink().put(parentListType.toString());
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.enumLangRoot1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(true);
            //this.displayTree();
        }

        protected void displayTree(){
            STRUCT_LIST_TYPE parentListType = ((STRUCT_LIST_TYPE)parentEnum);
            IGTree<IReadNode> listSink = parentListType.getListSink();
            System.out.println(parentEnum.toString() + ": LIST TYPE on pop");
            parentListType.getListSink().getParse().preOrder(listSink.getRoot(), new TaskDispReadNode());
            System.out.println(parentEnum.toString() + ": end display");
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(UNKNOWN_LANG_STRUCT);
            return false;
        }
    }

    // only allows leaves in path tree
    public static class ListScopeAlgo extends ListTypeAlgo {
        private static final String PATH_FORMAT = "%s" + LangConstants.PATH_TREE_SEP + "%s";

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            STRUCT_LIST_TYPE listType = (STRUCT_LIST_TYPE)parentEnum;
            String path = String.format(PATH_FORMAT, listType.toString(), currNode.text());
            listType.getListSink().put(path, currNode);
            return true;
        }
    }

    // with ListTypeAlgo: builds path tree to match given structure
    public static class Category extends AlgoBase{
        private static final String PATH_FORMAT = "%s" + LangConstants.PATH_TREE_SEP + "%s";
        private STRUCT_LIST_TYPE findListTypeBelow(IStackPayload stackTop){
            IStackPayload below = stackTop.getBelow();
            STRUCT_LIST_TYPE listType;
            LANG_STRUCT langStruct = below.getParentEnum();

            // Step 2: for later categories pushed, below = another category
            // Save the ordinal so next category can get it
            if(langStruct == CATEGORY){
                int ordinal = below.getState().getInt();
                stackTop.getState().set(ordinal);
                listType = STRUCT_LIST_TYPE.from(ordinal);
            }

            // Step 1: for first category pushed, below = LIST<TYPE>
            // Save the ordinal in state
            else{
                listType = (STRUCT_LIST_TYPE)langStruct;
                stackTop.getState().set(listType.ordinal());
            }
            return listType;
        }

        private String findCategoryPathBelow(String identifier, IStackPayload stackTop){
            IStackPayload below = stackTop.getBelow();

            // if below is a code block, skip it
            while(below.getParentEnumNonAlias() == Glob.ENUMS_BY_TYPE.enumCodeBlock()){
                below = below.getBelowNonAlias();
            }

            // Get path from below, add own
            LANG_STRUCT langStructBelow = below.getParentEnum();
            String category;
            if(langStructBelow == CATEGORY){
                String categoryBelow = below.getState().getString();
                category = String.format(PATH_FORMAT, categoryBelow, identifier);
            }
            else{
                category = String.format(PATH_FORMAT, langStructBelow.toString(), identifier);
            }
            stackTop.getState().set(category);
            return category;
        }

        private STRUCT_LIST_TYPE getListTypeFromState(IStackPayload stackPayload){
            int ordinal = stackPayload.getState().getInt();
            stackPayload.getState().set(ordinal);
            return STRUCT_LIST_TYPE.from(ordinal);
        }

        private String getPathFromState(IStackPayload stackPayload){
            return stackPayload.getState().getString();
        }

        @Override
        public void onPush(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            String identifier;
            if((identifier = currNode.textEvent().substring()) != null){// super.onPush() deletes the substring so get it now
                STRUCT_LIST_TYPE listType = this.findListTypeBelow(stackTop);
                //System.out.println("listType: " + listType);
                String path = this.findCategoryPathBelow(identifier, stackTop);
                //System.out.println("categoryPath: " + path);
                listType.getListSink().put(path, currNode);
            }
            onPush_checkIdentifierRule(stackTop);
        }

        @Override
        public void onPop(IStackPayload stackTop) {}

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            STRUCT_LIST_TYPE listType = this.getListTypeFromState(stackTop);
            String path = String.format(PATH_FORMAT, this.getPathFromState(stackTop), currNode.text());
            listType.getListSink().put(path, currNode);

            return true;
        }
    }

    // handles run attributes in key=value form; silent push/pop so it does not persist to the next parse step
    public static class RuntimeAttrib extends AlgoBase {
        // Silent push pop; check for err on identifier
        @Override
        public void onPush(IStackPayload stackTop) {
            this.onPush_checkIdentifierRule(stackTop);
        }

        @Override
        public void onPop(IStackPayload stackTop) {}

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode readNode = Glob.RUN_STATE.getCurrNode();
            Glob.ERR.check(RUNTIME_ATTRIB.props.put(readNode.text()));
            return false;
        }
    }

    // copies to alt data sink with tags (push, pop) removed; core task only copies current read node
    public static class AltSink extends AlgoBase{
        @Override
        public void onPush(IStackPayload stackTop) {
            this.onPush_checkIdentifierRule(stackTop);

            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.enumLangRoot1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(false);
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            this.onPop_checkIdentifierRule(stackTop);

            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.enumLangRoot1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(true);
        }
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.DATA_SINK.put();
            return true;
        }
    }

    // copies to alt data sink; err if more than one item included
    public static class AltSinkSingleItem extends AltSink {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            if(stackTop.getState().getInt() != FIRST){
                Glob.ERR.kill(ERR_TYPE.CONSTANT_NOT_SINGLE);
            }
            stackTop.getState().set(SECOND);
            Glob.DATA_SINK.put();
            return true;
        }
    }
}
