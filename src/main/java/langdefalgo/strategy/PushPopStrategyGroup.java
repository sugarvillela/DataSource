package langdefalgo.strategy;

import err.ERR_TYPE;
import langdef.CMD;
import langdefalgo.iface.IAlgoStrategy;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoBase;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import rule_follow.iface.IFollowRule;
import rule_identifier.iface.IIdentifierRule;
import runstate.Glob;
import stackpayload.iface.IStackPayload;
import textevent.impl.TextEvent;

public abstract class PushPopStrategyGroup {
    public static final PushStrategy PUT_PUSH_NODE = new OnPushPutNode();
    public static final PopStrategy PUT_POP_NODE = new OnPopPutNode();
    public static final PushStrategy IDENTIFY = new OnPushIdentify();
    public static final PopStrategy UN_IDENTIFY = new OnPopIdentify();
    public static final PushStrategy MUTE_ROOT = new OnPushMuteLangRoot();
    public static final PopStrategy UN_MUTE_ROOT = new OnPopUnMuteLangRoot();
    public static final PushStrategy FOLLOW_RULE = new OnPushFollowRule();
    public static final PushStrategy POP_NOW = new OnPushQuickPop();

    public static abstract class PushStrategy implements IAlgoStrategy{}
    public static abstract class PopStrategy implements IAlgoStrategy{}
    public static class OnPushPutNode extends PushStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {
            Glob.DATA_SINK.put();// push node
        }
    }
    public static class OnPopPutNode extends PopStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {
            IReadNode currNode = Glob.RUN_STATE.getCurrNode();
            IReadNode popNode = ReadNode.builder().copy(currNode).textEvent(
                    new TextEvent(algo.getParentEnum(), CMD.POP)
            ).build();
            Glob.DATA_SINK.put(popNode);// pop node
        }
    }
    public static class OnPushIdentify extends PushStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {
            IReadNode pushReadNode = Glob.RUN_STATE.getCurrNode();
            IIdentifierRule identifierRule = algo.getParentEnum().getIdentifierRule();
            if(identifierRule.onPush(pushReadNode)){// start listening to identifier, if allowed
                stackTop.getState().setPushedIdentifier(identifierRule.getPushedIdentifier());
            }
        }
    }
    public static class OnPopIdentify extends PopStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {
            String pushedIdentifier = stackTop.getState().getPushedIdentifier();// stop listening to identifier
            algo.getParentEnum().getIdentifierRule().onPop(pushedIdentifier);
        }
    }

    public static class OnPushMuteLangRoot extends PushStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {
            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.enumLangRoot1();
            Glob.DATA_SINK.getIdentifierOrErr(langRootEnum1.toString()).setListening(false);
        }
    }
    public static class OnPopUnMuteLangRoot extends PopStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {
            LANG_STRUCT langRootEnum1 = Glob.ENUMS_BY_TYPE.enumLangRoot1();
            Glob.DATA_SINK.getIdentifierOrErr(langRootEnum1.toString()).setListening(true);
        }
    }
    public static class OnPushFollowRule extends PushStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {//should wait until step 2 for code blocks to be deleted
            IFollowRule followrule = algo.getParentEnum().getFollowRule();
            if(!followrule.allAreAllowed()){
                //System.out.println("checkFollowRule: class = " + this.getClass().getSimpleName());
                int stackLevel = Glob.RUN_STATE.getStack().size() - 1;
                LANG_STRUCT prevItem;
                if(
                        (prevItem = Glob.RUN_STATE.getStack().getStackLog().lastIterationItem(stackLevel)) == null ||
                                !followrule.isAllowedPrev(prevItem)
                ){
                    Glob.ERR.kill(ERR_TYPE.SYNTAX);
                }
            }
        }
    }
    public static class OnPushQuickPop extends PushStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {
            Glob.RUN_STATE.getStack().pop();
        }
    }
    public static class OnPushCopy extends PushStrategy {
        @Override
        public void go(AlgoBase algo, IStackPayload stackTop) {
            Glob.DATA_SINK.put();
        }
    }
}
