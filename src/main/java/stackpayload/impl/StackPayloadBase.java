package stackpayload.impl;

import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoBase;
import langdefalgo.impl.AlgoProxy;
import runstate.Glob;
import stack.iface.IStackLog;
import stackpayload.iface.IPayloadState;
import stackpayload.iface.IStackPayload;

import java.util.ArrayList;

public abstract class StackPayloadBase implements IStackPayload {
    protected final LANG_STRUCT parentEnum;//, algo;
    protected final IPayloadState state;

    protected IStackPayload below;

    protected StackPayloadBase(LANG_STRUCT parentEnum, IPayloadState state) {
        if(parentEnum instanceof AlgoProxy || parentEnum instanceof AlgoBase){
            Glob.ERR_DEV.kill("Pass the enum to StackPayLoad");
        }
        this.parentEnum = parentEnum;
        this.state = state;
        state.init(this);
    }

    @Override
    public void addToStackLog(IStackLog stackLog) {// top level call
        ArrayList<LANG_STRUCT> newIteration = new ArrayList<>();
        if(below != null){                                          // populate bottom-first
            below.addToStackLog(stackLog, newIteration);
        }
        newIteration.add(this.getParentEnumNonAlias());  // add self
        stackLog.addIteration(newIteration);                        // save to stacklog
    }

    @Override
    public void addToStackLog(IStackLog stackLog, ArrayList<LANG_STRUCT> newIteration) {// recursive call
        if(below != null){                                          // populate bottom-first
            below.addToStackLog(stackLog, newIteration);
        }
        newIteration.add(this.getParentEnumNonAlias());  // add self
    }

    @Override
    public void setBelow(IStackPayload below) {
        this.below = below;
    }

    @Override
    public IPayloadState getState() {
        return state;
    }

    @Override
    public void go() {
        parentEnum.go(this);
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        return parentEnum;
    }

    @Override
    public LANG_STRUCT getParentEnumNonAlias() {// main implementation has no alias
        return parentEnum;
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
    public IStackPayload getBelow() {
        return below;
    }

    @Override
    public IStackPayload getBelowNonAlias() {
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
