package pushpoputil.impl;

import pushpoputil.iface.IPushPopUtil;
import stackpayload.iface.IStackPayload;

class PushPopUtilDisabled implements IPushPopUtil {
    @Override
    public boolean handleTextEvent(IStackPayload stackTop) {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }
}
