package langdef;

import rule_follow.iface.IFollowRule;
import rule_follow.impl.FollowRule;
import rule_identifier.iface.IIdentifierRule;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoBase;
import langdefalgo.impl.AlgoProxy;
import rule_nesting.iface.INestingRule;
import rule_nesting.impl.NestingRule;
import rule_pop.impl.PopRule;
import rule_pop.iface.IPopRule;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

import static rule_identifier.impl.IdentifierRuleImplGroup.ID_IGNORE;
import static langdef.LangConstants.*;

public enum STRUCT_LOOKUP  implements LANG_STRUCT, EnumPOJOJoin {
    ID_DEFINE   (DEFINE_START, ID_IGNORE),
    ID_ACCESS   (ACCESS_START, ID_IGNORE),
    COMMENT     (COMMENT_START, ID_IGNORE)
    ;

    private final String pushSymbol;
    private final IIdentifierRule identifierRule;
    private final AlgoProxy algoProxy;
    private final INestingRule nestingRule;
    private final IFollowRule followRule;
    private final IPopRule popRule;

    STRUCT_LOOKUP(char pushSymbol, IIdentifierRule identifierRule) {
        this.pushSymbol = String.valueOf(pushSymbol);
        this.identifierRule = identifierRule;
        this.algoProxy = new AlgoProxy();
        this.nestingRule = new NestingRule();
        this.followRule = new FollowRule();
        this.popRule = new PopRule();
    }

    @Override
    public String getPushSymbol() {
        return this.pushSymbol;
    }

    @Override
    public String getPopSymbol() {
        return null;
    }

    /*=====LANG_STRUCT================================================================================================*/

    @Override
    public boolean go(IStackPayload stackTop) {
        return algoProxy.go(stackTop);
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
    public IFollowRule getFollowRule() {
        return this.followRule;
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
