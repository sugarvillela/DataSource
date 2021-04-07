package pushpoputil.impl;

import langdef.CMD;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import pushpoputil.iface.IPushPopUtil;
import readnode.iface.IReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

class PushPopUtilEnabled implements IPushPopUtil {
    @Override
    public boolean handleTextEvent(IStackPayload stackTop) {
        IReadNode readNode = Glob.RUN_STATE.getCurrNode();
        if(readNode.hasTextEvent()){
            return (readNode.textEvent().cmd() == CMD.POP && tryPop(readNode, stackTop)) ||
                    (readNode.textEvent().cmd() == CMD.PUSH && tryPush(readNode, stackTop));
        }
        return false;
    }
    private boolean tryPop(IReadNode readNode, IStackPayload stackTop) {
        if(readNode.textEvent().langStruct() == Glob.ENUMS_BY_TYPE.enumLangS()){
            Glob.RUN_STATE.getStack().popMost();
        }
        else if(readNode.textEvent().langStruct() == Glob.ENUMS_BY_TYPE.enumCodeBlock()){
            Glob.RUN_STATE.getStack().pop();//pop twice
            if(Glob.RUN_STATE.getStack().top().getParentEnum().codeBlockRequired()){
                Glob.RUN_STATE.getStack().pop();
            }

        }
        else{
            Glob.RUN_STATE.getStack().pop();//pop one
        }
        return true;
    }
    private boolean tryPush(IReadNode readNode, IStackPayload stackTop) {
        LANG_STRUCT currTopEnum = stackTop.getParentEnum();
        LANG_STRUCT newTopEnum = readNode.textEvent().langStruct();
        if(
            (newTopEnum == Glob.ENUMS_BY_TYPE.enumCodeBlock() && currTopEnum.codeBlockRequired()) ||
            (currTopEnum == Glob.ENUMS_BY_TYPE.enumCodeBlock() && ((EnumPOJOJoin)currTopEnum).
                    getChildAlgo().getNestingRule().isAllowedNesting(newTopEnum)) ||
            currTopEnum.getNestingRule().isAllowedNesting(newTopEnum)
        ){
            IStackPayload newTopPayload = readNode.textEvent().langStruct().newStackPayload();
            Glob.RUN_STATE.getStack().push(newTopPayload);
            return true;
        }
        else{
            String message = String.format("Disallowed nesting: %s cannot be nested in %s",
                    readNode.textEvent().langStruct(),
                    stackTop.getParentEnum()
            );
            Glob.ERR.kill(message);
        }
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }
}
