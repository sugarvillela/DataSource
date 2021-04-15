package langdef;

import err.ERR_TYPE;
import langdefalgo.iface.IAlgoStrategy;
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

import static rule_identifier.impl.IdentifierRuleImplGroup.ID_IGNORE;

/** These are support structures used in implementation but not in the language front end. */
public enum STRUCT_NON_KEYWORD implements LANG_STRUCT, EnumPOJOJoin {
    LANG_T      (),
    SCOPE_TEST_ITEM(),
    RX_WORD     (),
    FX_WORD     (),
    LANG_ROOT_1(),
    LANG_ROOT_2(),
    ;

    private final AlgoProxy algoProxy;
    private final IIdentifierRule identifierRule;
    protected final INestingRule nestingRule;
    private final IFollowRule followRule;
    //private final IPopRule popRule;

    STRUCT_NON_KEYWORD() {
        this.algoProxy = new AlgoProxy();
        this.identifierRule = ID_IGNORE;
        this.nestingRule = new NestingRule();
        this.followRule = new FollowRule();
        //this.popRule = new PopRule();
    }

    /*=====LANG_STRUCT================================================================================================*/

    @Override
    public boolean go(IStackPayload stackTop) {
        return algoProxy.go(stackTop);
    }

    @Override
    public void onPush(IStackPayload stackTop) {
        algoProxy.onPush(stackTop);
    }

    @Override
    public void onPop(IStackPayload stackTop) {
        algoProxy.onPop(stackTop);
    }

    @Override
    public void onNest(IStackPayload newTop) {
        algoProxy.onNest(newTop);
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
    public boolean doCoreTask(IStackPayload stackTop) {
        return algoProxy.doCoreTask(stackTop);
    }

    @Override
    public boolean codeBlockRequired() {
        return false;
    }

    @Override
    public IStackPayload newStackPayload() {
        return algoProxy.newStackPayload();
    }

    /*=====EnumAlgoJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo, IAlgoStrategy[] pushes, IAlgoStrategy[] pops) {
        this.algoProxy.initAlgo(this, childAlgo, pushes, pops);
    }

    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo, IAlgoStrategy[] pushes, IAlgoStrategy[] pops) {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
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

}
