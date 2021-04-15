package langdefalgo.iface;

import langdefalgo.impl.AlgoBase;
import stackpayload.iface.IStackPayload;

public interface IAlgoStrategy {
    void go(AlgoBase algo, IStackPayload stackTop);
}
