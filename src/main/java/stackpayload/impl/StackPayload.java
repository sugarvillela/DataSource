package stackpayload.impl;

import langdefalgo.impl.AlgoBase;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoProxy;
import runstate.Glob;
import stackpayload.iface.IPayloadState;
import stackpayload.iface.IStackPayload;

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
