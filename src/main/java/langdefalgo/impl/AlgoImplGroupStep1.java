package langdefalgo.impl;

import attrib.types.RUNTIME_ATTRIB;
import langdef.CMD;
import langdef.STRUCT_LIST_TYPE;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;
import textevent.impl.TextEventNode;

import static err.ERR_TYPE.UNKNOWN_LANG_STRUCT;
import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LIST_TYPE.*;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.*;

public class AlgoImplGroupStep1 {
    public void initAlgos(){

        // Struct keyword  // no sharing singletons: each algo needs a unique state
        ATTRIB.initAlgo(new RuntimeAttrib());
        CONSTANT.initAlgo(new AltSink());
        RX.initAlgo(new CopyNode());
        FX.initAlgo(new CopyNode());
        FUN.initAlgo(new AltSink());
        SCOPE.initAlgo(new CopyNode());
        IF.initAlgo(new CopyNode());
        ELSE.initAlgo(new CopyNode());
        CATEGORY.initAlgo(new Category());
        INCLUDE.initAlgo(new CopyNode());

        // Struct list type
        LIST_STRING.initAlgo(new ListTypeAlgo());
        LIST_NUMBER.initAlgo(new ListTypeAlgo());
        LIST_BOOLEAN.initAlgo(new ListTypeAlgo());
        LIST_DISCRETE.initAlgo(new ListTypeAlgo());
        LIST_VOTE.initAlgo(new ListTypeAlgo());
        LIST_SCOPE.initAlgo(new ListTypeAlgo());

        // Struct lookup
        ID_DEFINE.initAlgo(new CopyNode());
        ID_ACCESS.initAlgo(new CopyNode());
        COMMENT.initAlgo(new CopyNode());

        // Struct non-keyword
        LANG_T.initAlgo(new CopyNode());
        IF_TEST.initAlgo(new CopyNode());
        SCOPE_TEST.initAlgo(new CopyNode());
        CONDITIONAL_ITEM.initAlgo(new CopyNode());
        RX_WORD.initAlgo(new CopyNode());
        FX_WORD.initAlgo(new CopyNode());
        LANG_ROOT_1.initAlgo(new CopyNode());
        LANG_ROOT_2.initAlgo(new CopyNode());

        // Struct symbol
        LANG_S.initAlgo(new CopyNode());
        LANG_T_INSERT.initAlgo(new CopyNode());
        ANTI_FX.initAlgo(new CopyNode());
    }

    public static class ListTypeAlgo extends AlgoBase{
        @Override
        public void onPush(IStackPayload stackTop) {
            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.langRootEnum1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(false);
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.langRootEnum1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(true);
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(UNKNOWN_LANG_STRUCT);
            return false;
        }
    }
    public static class Category extends AlgoBase{
        private STRUCT_LIST_TYPE findListType(IStackPayload stackTop){
            IStackPayload below = stackTop.getBelow();
            STRUCT_LIST_TYPE listType;
            LANG_STRUCT langStruct = below.getLangStructEnum();

            if(langStruct == CATEGORY){
                int ordinal = below.getState().getInt();
                listType = STRUCT_LIST_TYPE.from(ordinal);
            }
            else{
                listType = (STRUCT_LIST_TYPE)langStruct;
                stackTop.getState().set(listType.ordinal());
            }
            return listType;
        }
        private String findCategoryPath(String identifier, IStackPayload stackTop){
            IStackPayload below = stackTop.getBelow();
            LANG_STRUCT langStruct = below.getLangStructEnum();
            if(langStruct == CATEGORY){
                String categoryBelow = below.getState().getString();
                return String.format("%s.%s", categoryBelow, identifier);
            }
            else{
                stackTop.getState().set(identifier);
                return String.format("%s", identifier);
            }
        }
        @Override
        public void onPush(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            String identifier;
            if((identifier = currNode.textEvent().substring()) != null){// super.onPush() deletes the substring so get it now
                STRUCT_LIST_TYPE listType = this.findListType(stackTop);
                System.out.println("listType: " + listType);
                String categoryPath = this.findCategoryPath(identifier, stackTop);
                System.out.println("categoryPath: " + categoryPath);
            }
            super.onPush(stackTop); // identifier is required for this struct; will error at super.onPush() if null
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            IReadNode addNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(this.parentEnum, CMD.ADD_TO, currNode.text())).build();
            Glob.DATA_SINK.put(addNode);
            return false;
        }
    }

    public static class CopyNode extends AlgoBase {
        @Override
        public void onPop(IStackPayload stackTop) {
            //System.out.println("AlgoBase pop: " + this.getParentEnum());
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            if(this.getPopRule().isSelfPop()){
                this.onPop_putPopNode();
            }
            this.onPop_checkIdentifierRule(stackTop);
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            Glob.DATA_SINK.put();
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
        protected boolean doCoreTask(IStackPayload stackTop) {
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

            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.langRootEnum1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(false);

            //LANG_STRUCT langRootEnum2 = Glob.ENUMS_BY_TYPE.langRootEnum2();
            //Glob.DATA_SINK.getIdentifier(langRootEnum2.toString()).setListening(false);
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            this.onPop_checkIdentifierRule(stackTop);

            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.langRootEnum1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(true);

            //LANG_STRUCT langRootEnum2 = Glob.ENUMS_BY_TYPE.langRootEnum2();
            //Glob.DATA_SINK.getIdentifier(langRootEnum2.toString()).setListening(true);
        }
        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            Glob.DATA_SINK.put();
            return true;
        }
    }
}
