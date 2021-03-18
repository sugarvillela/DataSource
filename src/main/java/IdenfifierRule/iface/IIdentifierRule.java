package IdenfifierRule.iface;

import readnode.iface.IReadNode;

public interface IIdentifierRule {
    boolean ignore();
    boolean onPush(IReadNode pushedReadNode);
    void onPop(IReadNode pushedReadNode);
}
