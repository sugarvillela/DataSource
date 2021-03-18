package pushpoputil.iface;

import stackpayload.iface.IStackPayload;

public interface IPushPopUtil {
    boolean handleTextEvent(IStackPayload stackPayload);
    void setEnabled(boolean enabled);
}
