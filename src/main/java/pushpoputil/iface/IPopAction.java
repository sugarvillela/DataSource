package pushpoputil.iface;

import readnode.iface.IReadNode;
import stackpayload.iface.IStackPayload;

public interface IPopAction {
    boolean haveAction();
    void doAction(IStackPayload stackTop, IReadNode readNode);
}
