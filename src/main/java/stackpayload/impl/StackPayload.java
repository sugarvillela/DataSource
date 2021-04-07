package stackpayload.impl;

import langdefalgo.iface.LANG_STRUCT;
import stackpayload.iface.IPayloadState;

public class StackPayload extends StackPayloadBase {
    public StackPayload(LANG_STRUCT parentEnum) {
        this(parentEnum, new PayloadState());
    }
    public StackPayload(LANG_STRUCT parentEnum, IPayloadState state) {
        super(parentEnum, state);
    }
}
