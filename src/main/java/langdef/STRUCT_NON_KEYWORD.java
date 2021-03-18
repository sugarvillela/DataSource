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

import static IdenfifierRule.impl.IdentifierRuleImplGroup.ID_IGNORE;

/** These are support structures used in implementation but not in the language front end. */
public enum STRUCT_NON_KEYWORD implements LANG_STRUCT, EnumPOJOJoin {
    LANG_T      (),
    IF_ELSE     (),
    IF_TEST     (),
    SCOPE_TEST  (),
    SCOPE_ITEM  (),
    RX_WORD     (),
    FX_WORD     (),
    LANG_ROOT   (),
    ;

    private final AlgoProxy algoProxy;
    private final IIdentifierRule identifierRule;
    private final INestingRule nestingRule;
    private final IPopRule popRule;

    STRUCT_NON_KEYWORD() {
        this.algoProxy = new AlgoProxy();
        this.identifierRule = ID_IGNORE;
        this.nestingRule = new NestingRule();
        this.popRule = new PopRule();
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

    /*=====EnumAlgoJoin===============================================================================================*/

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

    /*=====TEXT_PATTERN===============================================================================================*/

    @Override
    public String getPushSymbol() {
        return null;
    }

    @Override
    public String getPopSymbol() {
        return null;
    }

    @Override
    public boolean isSelfPop() {
        return false;
    }

}
