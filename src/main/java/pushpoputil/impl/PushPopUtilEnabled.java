package pushpoputil.impl;

import err.ERR_TYPE;
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
            Glob.RUN_STATE.getStack().popMost();//pop all but LANG_S
        }
        else if(readNode.textEvent().langStruct() == Glob.ENUMS_BY_TYPE.enumCodeBlock()){
            Glob.RUN_STATE.getStack().pop();//pop twice
            if(Glob.RUN_STATE.getStack().top().getParentEnum().codeBlockRequired()){
                Glob.RUN_STATE.getStack().pop();
            }

        }
        else{
            Glob.RUN_STATE.getStack().pop();//pop once
        }
        return true;
    }

    private boolean tryPush(IReadNode readNode, IStackPayload stackTop) {
        LANG_STRUCT newTopEnum = readNode.textEvent().langStruct();

        if((newTopEnum == Glob.ENUMS_BY_TYPE.enumCodeBlock())){
            if(stackTop.getParentEnum().codeBlockRequired()){
                doPush(readNode);
                return true;
            }
            else{
                Glob.ERR.kill(ERR_TYPE.SYNTAX);
            }
        }
        //System.out.println("enum = " + stackTop.getSelf().getParentEnum());
        if(stackTop.getSelf().getParentEnum().getNestingRule().isAllowedNesting(newTopEnum)){
            doPush(readNode);
            return true;
        }

        String message = String.format("Disallowed nesting: %s cannot be nested in %s",
                readNode.textEvent().langStruct(),
                stackTop.getSelf().getParentEnum()
        );
        Glob.ERR.kill(message);
        return false;
    }

    private void doPush(IReadNode readNode){
        IStackPayload newTop = readNode.textEvent().langStruct().newStackPayload();
        Glob.RUN_STATE.getStack().push(newTop);
    }

    @Override
    public void setEnabled(boolean enabled) {}
}
