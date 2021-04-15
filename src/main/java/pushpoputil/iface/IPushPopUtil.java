package pushpoputil.iface;

import stackpayload.iface.IStackPayload;

public interface IPushPopUtil {
    boolean handleTextEvent(IStackPayload stackTop);
    void setEnabled(boolean enabled);
}
