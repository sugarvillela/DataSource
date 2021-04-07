package langdef;

import generictree.listsink.ListSink;
import generictree.iface.IGTree;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoBase;
import langdefalgo.impl.AlgoProxy;
import readnode.iface.IReadNode;
import rule_follow.iface.IFollowRule;
import rule_follow.impl.FollowRule;
import rule_identifier.iface.IIdentifierRule;
import rule_nesting.iface.INestingRule;
import rule_nesting.impl.NestingRule;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

import static rule_identifier.impl.IdentifierRuleImplGroup.ID_DISALLOW;

public enum STRUCT_LIST_TYPE implements LANG_STRUCT, EnumPOJOJoin {
    LIST_STRING,
    LIST_NUMBER,
    LIST_BOOLEAN,
    LIST_DISCRETE,
    LIST_VOTE,
    LIST_SCOPE
    ;

    private final IIdentifierRule identifierRule;
    protected final INestingRule nestingRule;
    private final AlgoProxy algoProxy;
    private final IFollowRule followRule;
    //private final IPopRule popRule;
    private final String pushSymbol;

    private final IGTree<IReadNode> listSink;

    STRUCT_LIST_TYPE() {
        this.identifierRule = ID_DISALLOW;
        this.nestingRule = new NestingRule();
        this.algoProxy = new AlgoProxy();
        this.followRule = new FollowRule();
        //this.popRule = new PopRule();
        this.pushSymbol = this.toString().replace('_', '<') + '>';// template-like notation
        this.listSink = new ListSink();
    }

    /*=====STRUCT_LIST_TYPE methods===================================================================================*/

    public static STRUCT_LIST_TYPE from(String text){
        try {
            return valueOf(text);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    public static STRUCT_LIST_TYPE from(int ordinal){
        try {
            return values()[ordinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    public IGTree<IReadNode> getListSink(){
        return listSink;
    }

    /*=====LANG_STRUCT================================================================================================*/

    @Override
    public String getPushSymbol() {
        return this.pushSymbol;
    }

    @Override
    public String getPopSymbol() {
        return "END_" + this.pushSymbol;
    }

    // everything below is identical to the other LANG_STRUCT enums
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
        return true;
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
