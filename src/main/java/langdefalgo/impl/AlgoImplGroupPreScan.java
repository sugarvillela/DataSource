package langdefalgo.impl;

import attrib.types.RUNTIME_ATTRIB;
import err.ERR_TYPE;
import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;
import rule_pop.iface.IPopAction;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;
import stackpayload.impl.PayloadStateAccStr;
import stackpayload.impl.StackPayload;
import textevent.impl.TextEventNode;

import static err.ERR_TYPE.UNKNOWN_LANG_STRUCT;
import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.*;

public class AlgoImplGroupPreScan {
    public static final int FIRST = 0, SECOND = 1;

    public void initPreScanAlgos(){

        // Struct keyword  // no sharing singletons: each algo needs a unique state
        ATTRIB.initAlgo(new Attrib());
        CONSTANT.initAlgo(new AltSinkNoTag());
        RX.initAlgo(new WordGroup(RX_WORD));
        FX.initAlgo(new WordGroup(FX_WORD));
        FUN.initAlgo(new AltSinkNoTag());
        SCOPE.initAlgo(new TestingStateMachine(SCOPE_TEST));
        IF.initAlgo(new TestingStateMachine(IF_TEST));
        ELSE.initAlgo(new ErrOnCoreTask());
        INCLUDE.initAlgo(new NoCoreTask());

        // Struct lookup
        ID_DEFINE.initAlgo(new NoCoreTask());
        ID_ACCESS.initAlgo(new NoCoreTask());
        COMMENT.initAlgo(new NoCoreTask());

        // Struct non-keyword
        LANG_T.initAlgo(new LangT());
        IF_TEST.initAlgo(new ConditionalTest(CONDITIONAL_ITEM));
        SCOPE_TEST.initAlgo(new ConditionalTest(CONDITIONAL_ITEM));
        CONDITIONAL_ITEM.initAlgo(new NoCoreTask());
        RX_WORD.initAlgo(new NoCoreTask());
        FX_WORD.initAlgo(new NoCoreTask());
        LANG_ROOT_1.initAlgo(new NoCoreTask());
        LANG_ROOT_2.initAlgo(new NoCoreTask());

        // Struct symbol
        LANG_S.initAlgo(new ErrOnCoreTask());
        LANG_T_INSERT.initAlgo(new LangT());
        ANTI_FX.initAlgo(new WordGroup(FX_WORD));
    }

    // placeholder for enum handled by another algo, or completed on push
    public static class NoCoreTask extends AlgoBase {
        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            return true;
        }
    }

    // enum only nests other enums; should never reach core task
    public static class ErrOnCoreTask extends AlgoBase {
        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(UNKNOWN_LANG_STRUCT);
            return false;
        }
    }

    // copies to alt data sink with tags (push, pop) removed; core task only copies current read node
    public static class AltSinkNoTag extends AlgoBase{
        @Override
        public void onPush(IStackPayload stackPayload) {
            this.onPush_checkIdentifierRule(stackPayload);

            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.langRootEnum1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(false);

            LANG_STRUCT langRootEnum2 = Glob.ENUMS_BY_TYPE.langRootEnum2();
            Glob.DATA_SINK.getIdentifier(langRootEnum2.toString()).setListening(false);
        }

        @Override
        public void onPop(IStackPayload stackPayload) {
            this.onPop_checkIdentifierRule(stackPayload);

            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.langRootEnum1();
            Glob.DATA_SINK.getIdentifier(langRootEnum1.toString()).setListening(true);

            LANG_STRUCT langRootEnum2 = Glob.ENUMS_BY_TYPE.langRootEnum2();
            Glob.DATA_SINK.getIdentifier(langRootEnum2.toString()).setListening(true);
        }
        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            Glob.DATA_SINK.put();
            return true;
        }
    }

    // de-tokenizes lines for direct copying of target language code
    public static class LangT extends AlgoBase {
        private void onEndLine(IStackPayload stackPayload){// reconstruct lines; Scan: no task
            String line = stackPayload.getState().getString();                      // accumulated string
            Glob.RUN_STATE.getCurrNode().setTextEvent(new TextEventNode(parentEnum, CMD.ADD_TO, line));// reconstruct line
            //System.out.println("LangT: doCoreTask: endLine: " + Glob.RUN_STATE.getCurrNode().csvString());
            Glob.DATA_SINK.put();

        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode readNode = Glob.RUN_STATE.getCurrNode();
            stackTop.getState().set(readNode.indentedText());                   // accumulate tokenized words
            if(readNode.endLine()){                                                 // if end line, reconstruct line
                this.onEndLine(stackTop);
            }
            return true;
        }

        @Override
        public void onPop(IStackPayload stackPayload) {
            this.onEndLine(stackPayload);                                           // dump accumulated before pop
            super.onPop(stackPayload);
        }

        @Override
        public IStackPayload newStackPayload() {
            return new StackPayload(this.parentEnum, new PayloadStateAccStr());
        }
    }

    // handles run attributes in key=value form; silent push/pop so it does not persist to the next parse step
    public static class Attrib extends AlgoBase {
        public Attrib() {
            super();
        }

        // Silent push pop; check for err on identifier
        @Override
        public void onPush(IStackPayload stackPayload) {
            this.onPush_checkIdentifierRule(stackPayload);
        }

        @Override
        public void onPop(IStackPayload stackPayload) {}

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode readNode = Glob.RUN_STATE.getCurrNode();
            Glob.ERR.check(RUNTIME_ATTRIB.props.put(readNode.text()));
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
        protected boolean doCoreTask(IStackPayload stackTop) {// add push/pop node without pushing to stack
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            //System.out.println("WordGroup core task");
            IReadNode wordPushNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(wordStruct, CMD.PUSH)).build();
            Glob.DATA_SINK.put(wordPushNode);

            IReadNode addNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(wordStruct, CMD.ADD_TO, currNode.text())).build();
            Glob.DATA_SINK.put(addNode);

            IReadNode wordPopNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(wordStruct, CMD.POP)).build();
            Glob.DATA_SINK.put(wordPopNode);
            return false;
        }
    }

    // Pushes and pops a test to handle conditional; then hosts the truthy part
    public static class TestingStateMachine extends AlgoBase {
        protected final LANG_STRUCT wordStruct;

        public TestingStateMachine(LANG_STRUCT wordStruct) {
            super();
            this.wordStruct = wordStruct;
        }

        @Override
        public void onPush(IStackPayload stackPayload) {
            super.onPush(stackPayload);
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            //System.out.println("TestingStateMachine onPush: "+ currNode.csvString());

            // push if_test or scope_test to sink
            IReadNode pushNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(wordStruct, CMD.PUSH)).build();
            Glob.RUN_STATE.goBack(pushNode);
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill(ERR_TYPE.BAD_TEST_PARAM);
            return false;
        }

        @Override
        public void onRegainTop() {}// gets called on scopeTest pop; keep just in case
    }

    // Handles conditional test for TestingStateMachine
    public static class ConditionalTest extends AlgoBase {
        // Nests RX or CONDITIONAL_ITEM, not both.
        // If RX, core task never called; ScopeTest.onRegainTop() backPops ScopeTest
        // Else if not RX, core task adds CONDITIONAL_ITEM push/pop to DataSink; PushPopUtil backPops ScopeTest on timeout
        private final LANG_STRUCT scopeItem;

        public ConditionalTest(LANG_STRUCT scopeItem) {
            super();
            this.scopeItem = scopeItem;
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {// add push/pop node without pushing to stack
            //System.out.println("Scope test doCoreTask start: "+ stackPayload.toString());
            int state = stackTop.getState().getInt();
            if(state == FIRST){
                stackTop.getState().set(SECOND);

                IReadNode currNode = Glob.RUN_STATE.getCurrNode();

                IReadNode itemPushNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(scopeItem, CMD.PUSH)).build();
                Glob.DATA_SINK.put(itemPushNode);

                IReadNode addNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(scopeItem, CMD.ADD_TO, currNode.text())).build();
                Glob.DATA_SINK.put(addNode);

                IReadNode itemPopNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(scopeItem, CMD.POP)).build();
                Glob.DATA_SINK.put(itemPopNode);
                //System.out.println("Scope test doCoreTask finish");
                return true;
            }
            else{
                Glob.ERR.kill(ERR_TYPE.SYNTAX);
            }
            //System.out.println("Scope test doCoreTask skipped");
            return false;
        }
        @Override
        public void onRegainTop() {
            IStackPayload stackTop = Glob.RUN_STATE.getStack().top();
            int state = stackTop.getState().getInt();

            if(state == FIRST){
                stackTop.getState().set(SECOND);

                IReadNode currNode = Glob.RUN_STATE.getCurrNode();
                IPopAction action = stackTop.getLangStructEnum().getPopRule().getPopAction(stackTop, currNode);
                if(action.haveAction()){
                    action.doAction(stackTop, currNode);
                }
            }
        }
    }

    /*=====Unused=====================================================================================================*/

    public static class ConditionalItem extends AlgoBase {
        public ConditionalItem() {
            super();
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            return false;
        }
    }

    public static class Rx extends AlgoBase {

        public Rx() {
            super();
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            if(!currNode.hasTextEvent()){}
            return false;
        }
    }
    public static class RxWord extends AlgoBase {

        public RxWord() {
            super();
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            return false;
        }
    }
    public static class Fx extends AlgoBase {

        public Fx() {
            super();
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            return false;
        }
    }
    public static class AntiFx extends AlgoBase {

        public AntiFx() {
            super();
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            return false;
        }
    }
    public static class FxWord extends AlgoBase {

        public FxWord() {
            super();
        }

        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            return false;
        }
    }
    public static class If extends TestingStateMachine {
        public If(LANG_STRUCT wordStruct) {
            super(wordStruct);
        }
    }
    public static class Scope extends AlgoBase {

        public Scope() {
            super();
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode readNode = Glob.RUN_STATE.getCurrNode();
            return false;
        }
    }
}
