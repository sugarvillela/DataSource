package langdefalgo.iface;

import rule_follow.iface.IFollowRule;
import rule_identifier.iface.IIdentifierRule;
import rule_nesting.iface.INestingRule;
import rule_pop.iface.IPopRule;
import stackpayload.iface.IStackPayload;

public interface LANG_STRUCT {
    String getPushSymbol();
    String getPopSymbol();
    boolean isSelfPop();

    boolean go(IStackPayload stackTop);

    void onPush(IStackPayload stackPayload);
    void onPop(IStackPayload stackPayload);
    void onRegainTop();

    IIdentifierRule getIdentifierRule();
    INestingRule getNestingRule();
    IFollowRule getFollowRule();
    IPopRule getPopRule();

    IStackPayload newStackPayload();

}
