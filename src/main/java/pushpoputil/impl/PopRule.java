package pushpoputil.impl;

import err.ERR_TYPE;
import langdef.CMD;
import pushpoputil.iface.IPopAction;
import pushpoputil.iface.IPopRule;
import readnode.iface.IReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

public class PopRule implements IPopRule {

    /*=====Static singletons (except popOnTimeOut) for rules, accessible via factory methods==========================*/

    private static IPopRule POP_ON_CMD;
    private static IPopRule ERR_ON_BAD_PUSH;
    private static IPopRule POP_ON_BAD_PUSH;

    public static IPopRule popOnCmd(){
        return (POP_ON_CMD == null)? (POP_ON_CMD = new PopOnCmd()) : POP_ON_CMD;
    }

    public static IPopRule errOnBadPush(){
        return (ERR_ON_BAD_PUSH == null)? (ERR_ON_BAD_PUSH = new ErrOnBadPush()) : ERR_ON_BAD_PUSH;
    }

    public static IPopRule popOnBadPush(){
        return (POP_ON_BAD_PUSH == null)? (POP_ON_BAD_PUSH = new PopOnBadPush()) : POP_ON_BAD_PUSH;
    }

    public static IPopRule popOnTimeOut(int timeOut){
        return new PopOnTimeOut(timeOut);
    }

    /*=====Static singletons for action strategies, accessible via factory methods====================================*/

    private static IPopAction ACTION_NONE;
    private static IPopAction ACTION_POP;
    private static IPopAction ACTION_POP_MOST;
    private static IPopAction ACTION_BACK_POP;
    private static IPopAction ACTION_SET_ERR;

    public static IPopAction actionNone(){
        return (ACTION_NONE == null)? (ACTION_NONE = new PopActionImplGroup.ActionNone()) : ACTION_NONE;
    }

    public static IPopAction actionPop(){
        return (ACTION_POP == null)? (ACTION_POP = new PopActionImplGroup.ActionPop()) : ACTION_POP;
    }

    public static IPopAction actionPopMost(){
        return (ACTION_POP_MOST == null)? (ACTION_POP_MOST = new PopActionImplGroup.ActionPopMost()) : ACTION_POP_MOST;
    }

    public static IPopAction actionBackPop(){
        return (ACTION_BACK_POP == null)? (ACTION_BACK_POP = new PopActionImplGroup.ActionBackPop()) : ACTION_BACK_POP;
    }

    public static IPopAction actionSetErr(){
        return (ACTION_SET_ERR == null)? (ACTION_SET_ERR = new PopActionImplGroup.ActionSetErr()) : ACTION_SET_ERR;
    }

    /*=====Exposed class: instantiated in LANG_STRUCT enum============================================================*/

    private IPopRule[] rules;

    @Override
    public void setRules(IPopRule... rules) {
        this.rules = rules;
    }

    @Override
    public IPopAction getPopAction(IStackPayload stackTop, IReadNode readNode) {
        IPopAction action;
        for(int i = 0; i < rules.length; i++){
            if((action = rules[i].getPopAction(stackTop, readNode )) != null){
                return action;
            }
        }
        return actionNone();
    }

    /*=====Rules======================================================================================================*/

    public static abstract class PopRuleBase  implements IPopRule {
        @Override
        public void setRules(IPopRule... rules) {
            Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
        }
    }

    public static class PopOnCmd extends PopRuleBase {
        @Override
        public IPopAction getPopAction(IStackPayload stackTop, IReadNode readNode) {
            if((readNode.hasTextEvent() && readNode.textEvent().cmd() == CMD.POP)){
                return (readNode.textEvent().langStruct() == Glob.ENUMS_BY_TYPE.sourceLangEnum())?
                        actionPopMost() : actionPop();
            }
            return null;
        }
    }

    public static class ErrOnBadPush extends PopRuleBase {
        @Override
        public IPopAction getPopAction(IStackPayload stackTop, IReadNode readNode) {
            if(readNode.hasTextEvent() && readNode.textEvent().cmd() == CMD.PUSH){
                return (stackTop.getLangStructEnum().getNestingRule().isAllowedNesting(readNode.textEvent().langStruct()))?
                        null : actionSetErr();
            }
            return null;
        }
    }

    public static class PopOnBadPush extends PopRuleBase {
        @Override
        public IPopAction getPopAction(IStackPayload stackTop, IReadNode readNode) {
            if(readNode.hasTextEvent() && (readNode.textEvent().cmd() == CMD.PUSH || readNode.textEvent().cmd() == CMD.POP)){
                return (stackTop.getLangStructEnum().getNestingRule().isAllowedNesting(readNode.textEvent().langStruct()))?
                        null : actionBackPop();
            }
            return null;
        }
    }

    public static class PopOnTimeOut extends PopRuleBase {
        private final int timeOut;

        public PopOnTimeOut(int timeOut) {
            this.timeOut = timeOut;
        }

        @Override
        public IPopAction getPopAction(IStackPayload stackTop, IReadNode readNode) {
            return (stackTop.getState().timeOnStack() > timeOut)?
                    actionBackPop() : null;
        }
    }
}
