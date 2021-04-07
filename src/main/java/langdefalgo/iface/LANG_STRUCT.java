package langdefalgo.iface;

import rule_follow.iface.IFollowRule;
import rule_identifier.iface.IIdentifierRule;
import rule_nesting.iface.INestingRule;
import stackpayload.iface.IStackPayload;

public interface LANG_STRUCT {
    String getPushSymbol();
    String getPopSymbol();

    boolean go(IStackPayload stackTop);

    void onPush(IStackPayload stackPayload);
    void onPop(IStackPayload stackPayload);
    void onRegainTop();

    IIdentifierRule getIdentifierRule();
    INestingRule getNestingRule();
    IFollowRule getFollowRule();

    boolean codeBlockRequired();

    IStackPayload newStackPayload();

}
