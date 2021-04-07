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
import runstate.Glob;
import stackpayload.iface.IStackPayload;

import static rule_identifier.impl.IdentifierRuleImplGroup.*;

public enum STRUCT_KEYWORD implements LANG_STRUCT, EnumPOJOJoin {
    ATTRIB      (ID_DISALLOW),
    CONSTANT    (ID_REQUIRE),
    RX          (ID_ALLOW),
    FX          (ID_ALLOW),
    FUN         (ID_REQUIRE),
    SCOPE       (ID_ALLOW),
    IF          (ID_ALLOW),
    ELSE        (ID_ALLOW),
    CATEGORY    (ID_REQUIRE_SILENT),

    INCLUDE     (ID_DISALLOW, false),
    ;

    private final IIdentifierRule identifierRule;
    protected final INestingRule nestingRule;
    private final AlgoProxy algoProxy;
    private final IFollowRule followRule;
    //private final IPopRule popRule;
    private final boolean codeBlockRequired;

    STRUCT_KEYWORD(IIdentifierRule identifierRule){
        this(identifierRule, true);
    }
    STRUCT_KEYWORD(IIdentifierRule identifierRule, boolean codeBlockRequired) {
        this.codeBlockRequired = codeBlockRequired;
        this.identifierRule = identifierRule;
        this.nestingRule = new NestingRule();
        this.algoProxy = new AlgoProxy();
        this.followRule = new FollowRule();
        //this.popRule = new PopRule();
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
        return nestingRule;
    }

    @Override
    public IFollowRule getFollowRule() {
        return this.followRule;
    }

    @Override
    public boolean codeBlockRequired() {
        return codeBlockRequired;
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
