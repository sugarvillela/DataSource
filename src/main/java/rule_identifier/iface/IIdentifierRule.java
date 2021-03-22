package rule_identifier.iface;

import readnode.iface.IReadNode;

public interface IIdentifierRule {
    boolean ignore();
    boolean onPush(IReadNode pushedReadNode);
    void onPop(String pushedIdentifier);

    String getPushedIdentifier();
}
