package langdefalgo.iface;

import IdenfifierRule.iface.IIdentifierRule;
import nestingrule.iface.INestingRule;
import pushpoputil.iface.IPopRule;
import stackpayload.iface.IStackPayload;

public interface LANG_STRUCT {
    String getPushSymbol();
    String getPopSymbol();
    boolean isSelfPop();

    boolean go(IStackPayload stackPayload);

    void onPush(IStackPayload stackPayload);
    void onPop(IStackPayload stackPayload);
    void onRegainTop();

    IIdentifierRule getIdentifierRule();
    INestingRule getNestingRule();
    IPopRule getPopRule();

    IStackPayload newStackPayload();

}
