package pushpoputil.impl;

import pushpoputil.iface.IPopAction;
import readnode.iface.IReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

public class PopActionImplGroup {
    /*
    ACTION_NONE,
    ACTION_POP,
    ACTION_POP_MOST,
    ACTION_BACK_POP,
    ACTION_SET_ERR
    * */
    public static abstract class PopActionBase implements IPopAction {
        @Override
        public boolean haveAction() {
            return true;
        }
    }

    public static class ActionNone extends PopActionBase {
        @Override
        public boolean haveAction() {
            return false;
        }

        @Override
        public void doAction(IStackPayload stackTop, IReadNode readNode) {}
    }

    public static class ActionPop extends PopActionBase {
        @Override
        public void doAction(IStackPayload stackTop, IReadNode readNode) {
            Glob.RUN_STATE.pop();
        }
    }

    public static class ActionPopMost extends PopActionBase {
        @Override
        public void doAction(IStackPayload stackTop, IReadNode readNode) {
            Glob.RUN_STATE.popMost();
        }
    }

    public static class ActionBackPop extends PopActionBase {
        @Override
        public void doAction(IStackPayload stackTop, IReadNode readNode) {
            Glob.RUN_STATE.goBack(readNode);
            Glob.RUN_STATE.pop();
        }
    }

    public static class ActionSetErr extends PopActionBase {
        @Override
        public void doAction(IStackPayload stackTop, IReadNode readNode) {
            String message = String.format("Disallowed nesting: %s cannot be nested in %s",
                    readNode.textEvent().langStruct(),
                    stackTop.getLangStructEnum()
            );
            Glob.ERR.kill(message);
        }
    }
}
