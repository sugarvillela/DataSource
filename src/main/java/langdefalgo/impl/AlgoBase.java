package langdefalgo.impl;

import err.ERR_TYPE;
import langdef.CMD;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.IAlgoStrategy;
import langdefalgo.iface.LANG_STRUCT;
import rule_follow.iface.IFollowRule;
import rule_nesting.iface.INestingRule;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;
import stackpayload.impl.StackPayload;
import rule_identifier.iface.IIdentifierRule;
import textevent.impl.TextEvent;

public abstract class AlgoBase implements LANG_STRUCT, EnumPOJOJoin {
    protected IAlgoStrategy[] pushes, pops;
    protected LANG_STRUCT parentEnum;

    protected AlgoBase() {

    }

    @Override
    public boolean go(IStackPayload stackTop) {
        return Glob.PUSH_POP_UTIL.handleTextEvent(stackTop) || doCoreTask(stackTop);
    }

    @Override
    public void onPush(IStackPayload stackTop) {
        for(IAlgoStrategy strategy : pushes){
            strategy.go(this, stackTop);
        }
    }

    @Override
    public void onPop(IStackPayload stackTop) {
        for(IAlgoStrategy strategy : pops){
            strategy.go(this, stackTop);
        }
    }

    @Override
    public void onNest(IStackPayload newTop) {
        //System.out.println("OnNest: new top = " + newTop.toString());
    }

    @Override
    public void onRegainTop() {}

    @Override
    public IIdentifierRule getIdentifierRule(){
        return parentEnum.getIdentifierRule();
    }

    @Override
    public INestingRule getNestingRule() {
        return parentEnum.getNestingRule();
    }

    @Override
    public IFollowRule getFollowRule() {
        return parentEnum.getFollowRule();
    }

    @Override
    public boolean codeBlockRequired() {
        return parentEnum.codeBlockRequired();
    }

    @Override
    public IStackPayload newStackPayload() {
        return new StackPayload(this.parentEnum);
    }

    /*=====EnumAlgoJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo, IAlgoStrategy[] pushes, IAlgoStrategy[] pops) {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
    }
    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo, IAlgoStrategy[] pushes, IAlgoStrategy[] pops) {
        this.parentEnum = parentEnum;
        this.pushes = pushes;
        this.pops = pops;
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        return parentEnum;
    }

    @Override
    public LANG_STRUCT getChildAlgo() {
        Glob.ERR_DEV.kill("Call getChildAlgo only on parent enum");
        return null;
    }

    /*=====TEXT_PATTERN===============================================================================================*/

    @Override
    public String getPushSymbol() {
        Glob.ERR_DEV.kill("This should be called on the parent enum");
        return null;
    }

    @Override
    public String getPopSymbol() {
        Glob.ERR_DEV.kill("This should be called on the parent enum");
        return null;
    }

}
