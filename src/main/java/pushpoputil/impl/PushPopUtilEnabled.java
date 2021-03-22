package pushpoputil.impl;

import langdef.CMD;
import rule_pop.iface.IPopAction;
import pushpoputil.iface.IPushPopUtil;
import readnode.iface.IReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

class PushPopUtilEnabled implements IPushPopUtil {
    @Override
    public boolean handleTextEvent(IStackPayload stackTop) {
        IReadNode readNode = Glob.RUN_STATE.getCurrNode();
        IPopAction action = stackTop.getLangStructEnum().getPopRule().getPopAction(stackTop, readNode);
        if(action.haveAction()){
            action.doAction(stackTop, readNode);
            return true;
        }
        if(readNode.hasTextEvent() && readNode.textEvent().cmd() == CMD.PUSH){
            IStackPayload newTop = readNode.textEvent().langStruct().newStackPayload();
            Glob.RUN_STATE.push(newTop);
            return true;
        }
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }
}
