package langdef;

import IdenfifierRule.iface.IIdentifierRule;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoBase;
import langdefalgo.impl.AlgoProxy;
import nestingrule.iface.INestingRule;
import nestingrule.impl.NestingRule;
import pushpoputil.impl.PopRule;
import pushpoputil.iface.IPopRule;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

import static IdenfifierRule.impl.IdentifierRuleImplGroup.*;

public enum STRUCT_KEYWORD implements LANG_STRUCT, EnumPOJOJoin {
    ATTRIB      (ID_DISALLOW),
    CONSTANT    (ID_REQUIRE),
    RX          (ID_ALLOW),
    FX          (ID_ALLOW),
    RXFX        (ID_ALLOW),
    FUN         (ID_REQUIRE),
    SCOPE       (ID_ALLOW),
    IF          (ID_ALLOW),
    ELSE        (ID_ALLOW),

    INCLUDE     (ID_DISALLOW),
    ;

    private final IIdentifierRule identifierRule;
    private final AlgoProxy algoProxy;
    private final INestingRule nestingRule;
    private final IPopRule popRule;

    STRUCT_KEYWORD(IIdentifierRule identifierRule) {
        this.identifierRule = identifierRule;
        this.algoProxy = new AlgoProxy();
        this.nestingRule = new NestingRule();
        this.popRule = new PopRule();
    }

    /*=====TEXT_PATTERN===============================================================================================*/

    @Override
    public String getPushSymbol() {
        return this.toString();
    }

    @Override
    public String getPopSymbol() {
        return "END_" + this.toString();
    }

    @Override
    public boolean isSelfPop() {
        return false;
    }

    /*=====LANG_STRUCT================================================================================================*/

    @Override
    public boolean go(IStackPayload stackPayload) {
        return algoProxy.go(stackPayload);
    }

    @Override
    public void onPush(IStackPayload stackPayload) {
        algoProxy.onPush(stackPayload);
    }

    @Override
    public void onPop(IStackPayload stackPayload) {
        algoProxy.onPop(stackPayload);
    }

    @Override
    public void onRegainTop() {
        algoProxy.onRegainTop();
    }

    @Override
    public IIdentifierRule getIdentifierRule() {
        return this.identifierRule;
    }

    @Override
    public INestingRule getNestingRule() {
        return this.nestingRule;
    }

    @Override
    public IPopRule getPopRule() {
        return this.popRule;
    }

    @Override
    public IStackPayload newStackPayload() {
        return algoProxy.newStackPayload();
    }

    /*=====EnumPOJOJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo) {
        this.algoProxy.initAlgo(this, childAlgo);
    }

    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo) {
        Glob.ERR_DEV.kill("Call two-arg initAlgo() only on child algo");
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        Glob.ERR_DEV.kill("Call getParentEnum only on child algo");
        return null;
    }

    @Override
    public LANG_STRUCT getChildAlgo() {
        return algoProxy;
    }
}
