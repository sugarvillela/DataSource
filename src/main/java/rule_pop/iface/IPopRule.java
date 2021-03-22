package rule_pop.iface;

import readnode.iface.IReadNode;
import stackpayload.iface.IStackPayload;

public interface IPopRule {
    void setRules(IPopRule... rules);
    IPopAction getPopAction(IStackPayload stackTop, IReadNode readNode);
}
