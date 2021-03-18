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
import static langdef.LangConstants.*;


public enum STRUCT_SYMBOL implements LANG_STRUCT, EnumPOJOJoin {
    LANG_S          (OPEN_S,        CLOSE_S, ID_DISALLOW),
    LANG_T_INSERT   (OPEN_T,        CLOSE_T, ID_IGNORE),
    ANTI_FX         (SYM_ANTI_FX,   SYM_END_ANTI_FX, ID_ALLOW),
    ;

    private final String pushSymbol, popSymbol;
    private final IIdentifierRule identifierRule;
    private final AlgoProxy algoProxy;
    private final INestingRule nestingRule;
    private final IPopRule popRule;

    STRUCT_SYMBOL(String pushSymbol, String popSymbol, IIdentifierRule identifierRule) {
        this.pushSymbol = pushSymbol;
        this.popSymbol = popSymbol;
        this.identifierRule = identifierRule;
        this.algoProxy = new AlgoProxy();
        this.nestingRule = new NestingRule();
        this.popRule = new PopRule();
    }

    @Override
    public String getPushSymbol() {
        return this.pushSymbol;
    }

    @Override
    public String getPopSymbol() {
        return this.popSymbol;
    }

    @Override
    public boolean isSelfPop() {
        return true;
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

}
