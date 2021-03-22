package stackpayload.impl;

import langdefalgo.impl.AlgoBase;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoProxy;
import runstate.Glob;
import stack.iface.IStackLog;
import stack.iface.IStackLogIterationItem;
import stack.impl.StackLogIterationItem;
import stackpayload.iface.IPayloadState;
import stackpayload.iface.IStackPayload;

import java.util.ArrayList;

public class StackPayload implements IStackPayload {
    private final LANG_STRUCT parentEnum;//, algo;
    private final IPayloadState state;

    private IStackPayload below;

    public StackPayload(LANG_STRUCT parentEnum) {
        this(parentEnum, new PayloadState());
    }
    public StackPayload(LANG_STRUCT parentEnum, IPayloadState state) {
        if(parentEnum instanceof AlgoProxy || parentEnum instanceof AlgoBase){
            Glob.ERR_DEV.kill("Pass the enum to StackPayLoad");
        }
        state.init(this);
        this.parentEnum = parentEnum;
        this.state = state;
    }

    @Override
    public void go() {
        parentEnum.go(this);
    }

    @Override
    public LANG_STRUCT getLangStructEnum() {
        return parentEnum;
    }

    @Override
    public IPayloadState getState() {
        return state;
    }

    @Override
    public void onPush() {
        parentEnum.onPush(this);
    }

    @Override
    public void onPop() {
        parentEnum.onPop(this);
    }

    @Override
    public void addToStackLog(IStackLog stackLog, ArrayList<IStackLogIterationItem> newIteration) {
        if(newIteration == null){               // top of stack - build, populate and save to stackLog
            newIteration = new ArrayList<>();
            if(below != null){                  // populate bottom-first
                below.addToStackLog(stackLog, newIteration);
            }
            newIteration.add(new StackLogIterationItem(this.getLangStructEnum()));  // add self
            stackLog.addIteration(newIteration);// save to stacklog
        }
        else{
            if(below != null){                  // populate bottom-first
                below.addToStackLog(stackLog, newIteration);
            }
            newIteration.add(new StackLogIterationItem(this.getLangStructEnum()));  // add self
        }
    }

    @Override
    public void setBelow(IStackPayload below) {
        this.below = below;
    }

    @Override
    public IStackPayload getBelow() {
        return below;
    }

    @Override
    public String toString() {
        return "StackPayload{" +
                parentEnum +
                ", state=(" + state.toString() + ")" +
                '}';
    }
}
