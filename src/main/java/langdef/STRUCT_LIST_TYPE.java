package langdef;

import err.ERR_TYPE;
import generictree.listsink.ListTree;
import generictree.iface.IGTree;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.IAlgoStrategy;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoBase;
import langdefalgo.impl.AlgoProxy;
import langdefsub.PRIM_TYPE;
import readnode.iface.IReadNode;
import rule_follow.iface.IFollowRule;
import rule_follow.impl.FollowRule;
import rule_identifier.iface.IIdentifierRule;
import rule_nesting.iface.INestingRule;
import rule_nesting.impl.NestingRule;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

import static rule_identifier.impl.IdentifierRuleImplGroup.ID_DISALLOW;
import static langdefsub.PRIM_TYPE.*;

public enum STRUCT_LIST_TYPE implements LANG_STRUCT, EnumPOJOJoin {
    LIST_STRING     (STRING),
    LIST_NUMBER     (NUMBER),
    LIST_BOOLEAN    (BOOLEAN),
    LIST_DISCRETE   (DISCRETE),
    LIST_VOTE       (NUMBER),
    LIST_SCOPE      (STRING)
    ;

    private final PRIM_TYPE outType;
    private final IIdentifierRule identifierRule;
    protected final INestingRule nestingRule;
    private final AlgoProxy algoProxy;
    private final IFollowRule followRule;
    private final String pushSymbol;

    private final IGTree<IReadNode> listTree;

    STRUCT_LIST_TYPE(PRIM_TYPE outType) {
        this.outType = outType;
        this.identifierRule = ID_DISALLOW;
        this.nestingRule = new NestingRule();
        this.algoProxy = new AlgoProxy();
        this.followRule = new FollowRule();
        this.pushSymbol = this.toString().replace('_', '<') + '>';// template-like notation
        this.listTree = new ListTree();
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

    public IGTree<IReadNode> getListTree(){
        return listTree;
    }
    public PRIM_TYPE outType() {
        return outType;
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
        return true;
    }

    @Override
    public IStackPayload newStackPayload() {
        return algoProxy.newStackPayload();
    }

    /*=====EnumPOJOJoin===============================================================================================*/

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
