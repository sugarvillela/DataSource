package pushpoputil.impl;

import pushpoputil.iface.IPushPopUtil;
import stackpayload.iface.IStackPayload;

public class PushPopUtil implements IPushPopUtil {
    private static PushPopUtil instance;

    public static PushPopUtil initInstance(){
        return (instance == null)? (instance = new PushPopUtil()): instance;
    }

    private final IPushPopUtil enabled, disabled;
    private IPushPopUtil currUtil;

    private PushPopUtil(){
        currUtil = enabled = new PushPopUtilEnabled();
        disabled = new PushPopUtilDisabled();
    }

    @Override
    public boolean handleTextEvent(IStackPayload stackPayload) {
        return currUtil.handleTextEvent(stackPayload);
    }

    @Override
    public void setEnabled(boolean enabled) {
        currUtil = (enabled)? this.enabled : this.disabled;
    }
}
