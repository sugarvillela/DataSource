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

import static rule_identifier.impl.IdentifierRuleImplGroup.*;
import static langdef.LangConstants.*;


public enum STRUCT_SYMBOL implements LANG_STRUCT, EnumPOJOJoin {
    LANG_S          (OPEN_S,        CLOSE_S,        ID_DISALLOW),
    LANG_T_INSERT   (OPEN_T,        CLOSE_T,        ID_IGNORE),
    SCOPE_TEST      (COND_OPEN,     COND_CLOSE,     ID_DISALLOW),
    CODE_BLOCK      (ITEM_OPEN,     ITEM_CLOSE,     ID_DISALLOW),
    A_FX            (SYM_A_FX,      SYM_END_A_FX,   ID_ALLOW, true),
    ;

    private final String pushSymbol, popSymbol;
    private final IIdentifierRule identifierRule;
    protected final INestingRule nestingRule;
    private final AlgoProxy algoProxy;
    private final IFollowRule followRule;
    //private final IPopRule popRule;
    private final boolean codeBlockRequired;

    STRUCT_SYMBOL(String pushSymbol, String popSymbol, IIdentifierRule identifierRule){
        this(pushSymbol, popSymbol, identifierRule,false);
    }
    STRUCT_SYMBOL(String pushSymbol, String popSymbol, IIdentifierRule identifierRule, boolean codeBlockRequired) {
        this.pushSymbol = pushSymbol;
        this.popSymbol = popSymbol;
        this.identifierRule = identifierRule;
        this.nestingRule = new NestingRule();
        this.codeBlockRequired = codeBlockRequired;
        this.algoProxy = new AlgoProxy();
        this.followRule = new FollowRule();
        //this.popRule = new PopRule();
    }

    @Override
    public String getPushSymbol() {
        return this.pushSymbol;
    }

    @Override
    public String getPopSymbol() {
        return this.popSymbol;
    }

    /*=====LANG_STRUCT================================================================================================*/

    @Override
    public boolean go(IStackPayload stackTop) {
        return algoProxy.go(stackTop);
    }

    @Override
    public void onPush(IStackPayload stackTop) {
        //System.out.println("STRUCT_SYMBOL push: ENUM = " + this);
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
        return codeBlockRequired;
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

}
