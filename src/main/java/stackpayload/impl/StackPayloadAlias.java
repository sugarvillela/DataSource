package stackpayload.impl;

import langdefalgo.iface.LANG_STRUCT;
import stackpayload.iface.IStackPayload;

public class StackPayloadAlias extends StackPayloadBase{
    public StackPayloadAlias(LANG_STRUCT parentEnum) {
        super(parentEnum, new PayloadState());
    }

    @Override
    public IStackPayload getBelow() {
        return below.getBelow();
    }

    @Override
    public IStackPayload getBelowNonAlias() {
        return below;
    }

    @Override
    public void go() {
        below.go();
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        return below.getParentEnum();
    }

    @Override
    public LANG_STRUCT getParentEnumNonAlias() {
        return parentEnum;
    }
}
