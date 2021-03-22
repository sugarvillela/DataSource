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

import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.*;

public class AlgoImplGroupPreScan {
    public static final int FIRST = 0, SECOND = 1, PARSE = 2;

    public void initPreScanAlgos(){

        // Struct keyword  // no sharing singletons: each algo needs a unique state
        ATTRIB.initAlgo(new Attrib());
        CONSTANT.initAlgo(new Constant());
        RX.initAlgo(new WordGroup(RX_WORD));
        FX.initAlgo(new WordGroup(FX_WORD));
        FUN.initAlgo(new Fun());
        SCOPE.initAlgo(new TestingStateMachine(SCOPE_TEST));
        IF.initAlgo(new TestingStateMachine(IF_TEST));
        ELSE.initAlgo(new Else());
        INCLUDE.initAlgo(new Nop());

        // Struct lookup
        ID_DEFINE.initAlgo(new Nop());
        ID_ACCESS.initAlgo(new IdAccess());
        COMMENT.initAlgo(new Nop());

        // Struct non-keyword
        LANG_T.initAlgo(new LangT());
        IF_TEST.initAlgo(new ConditionalTest(CONDITIONAL_ITEM));
        SCOPE_TEST.initAlgo(new ConditionalTest(CONDITIONAL_ITEM));
        CONDITIONAL_ITEM.initAlgo(new ConditionalItem());
        RX_WORD.initAlgo(new Nop());
        FX_WORD.initAlgo(new Nop());
        LANG_ROOT.initAlgo(new Nop());

        // Struct symbol
        LANG_S.initAlgo(new LangS());
        LANG_T_INSERT.initAlgo(new LangTInsert());
        ANTI_FX.initAlgo(new WordGroup(FX_WORD));
    }

    public static class Nop extends AlgoBase {
        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            return false;
        }
    }

    public static class LangT extends AlgoBase {
        public LangT() {
            super();
        }

        private void onEndLine(IStackPayload stackPayload){// reconstruct lines; Scan: no task
            String line = stackPayload.getState().getString();                      // accumulated string
            Glob.RUN_STATE.getCurrNode().setTextEvent(new TextEventNode(parentEnum, CMD.ADD_TO, line));// reconstruct line

            Glob.DATA_SINK.put();
            //System.out.println("LangT: doCoreTask: endLine: " + line);
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
    public static class LangTInsert extends LangT { }

    // No task
    public static class LangS extends AlgoBase {
        public LangS() {
            super();
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            Glob.ERR.kill("Unknown keyword");
            return false;
        }
    }

    // PreScan: handle run attrib; Scan: deleted
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

    // RX, FX, ANTI_FX
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
        public void onRegainTop() {}// gets called on scopeTest pop
    }

    public static class Else extends AlgoBase {

        public Else() {
            super();
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            return false;
        }
    }

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

    // pass
    public static class IdAccess extends AlgoBase {

//        public IdAccess() {
//            super();
//        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            System.out.println("IdAccess: doCoreTask: " + currNode.text());
            return false;
        }
    }

    /*=====Route output to alt dataSink===============================================================================*/

    public static abstract class AltSinkAlgo extends AlgoBase{
        @Override
        public void onPush(IStackPayload stackPayload) {
            this.onPush_checkIdentifierRule(stackPayload);

            LANG_STRUCT langRootEnum = Glob.ENUMS_BY_TYPE.langRootEnum();
            Glob.DATA_SINK.getIdentifier(langRootEnum.toString()).setListening(false);
        }

        @Override
        public void onPop(IStackPayload stackPayload) {
            this.onPop_checkIdentifierRule(stackPayload);

            LANG_STRUCT langRootEnum = Glob.ENUMS_BY_TYPE.langRootEnum();
            Glob.DATA_SINK.getIdentifier(langRootEnum.toString()).setListening(true);
        }
    }
    public static class Constant extends AltSinkAlgo {
//        public Constant() {
//            super();
//        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            Glob.DATA_SINK.put();
            return true;
        }
    }
    public static class Fun extends AltSinkAlgo {

//        public Fun() {
//            super();
//        }
        private boolean shouldPop(){
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            return currNode.hasTextEvent() &&
                    currNode.textEvent().cmd() == CMD.POP &&
                    currNode.textEvent().langStruct() == parentEnum;
        }

        @Override
        protected boolean doCoreTask(IStackPayload stackTop) {
            if(this.shouldPop()){
                Glob.RUN_STATE.pop();
                return true;
            }
            else{
//                this.eventToCurrNode_addTo();
                Glob.DATA_SINK.put();
                return false;
            }
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

    public static abstract class SilentPushPop extends AlgoBase{
        @Override
        public void onPush(IStackPayload stackPayload) {}

        @Override
        public void onPop(IStackPayload stackPayload) {}
    }
}
