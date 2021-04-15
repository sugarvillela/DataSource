package langdefalgo.iface;

import rule_follow.iface.IFollowRule;
import rule_identifier.iface.IIdentifierRule;
import rule_nesting.iface.INestingRule;
import stackpayload.iface.IStackPayload;

public interface LANG_STRUCT {
    String getPushSymbol();
    String getPopSymbol();

    boolean go(IStackPayload stackTop);

    void onPush(IStackPayload stackTop);
    void onPop(IStackPayload stackTop);
    void onNest(IStackPayload newTop);
    void onRegainTop();

    IIdentifierRule getIdentifierRule();
    INestingRule getNestingRule();
    IFollowRule getFollowRule();

    boolean doCoreTask(IStackPayload stackTop);

    boolean codeBlockRequired();

    IStackPayload newStackPayload();

}
