package langdefalgo.impl;

import stackpayload.iface.IStackPayload;
import stackpayload.impl.StackPayloadAlias;

public class AlgoCodeBlock extends AlgoBase {
    @Override
    public IStackPayload newStackPayload() {
        return new StackPayloadAlias(this.parentEnum);
    }

    @Override
    public boolean doCoreTask(IStackPayload stackTop) {
        return false;
    }

    @Override // silent push; but check for misplaced identifier
    public void onPush(IStackPayload stackTop) {
        onPush_checkIdentifierRule(stackTop);
    }

    @Override // silent pop
    public void onPop(IStackPayload stackTop) {}
}
