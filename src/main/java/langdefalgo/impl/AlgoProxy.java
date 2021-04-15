package langdefalgo.impl;

import err.ERR_TYPE;
import langdefalgo.iface.IAlgoStrategy;
import rule_follow.iface.IFollowRule;
import rule_identifier.iface.IIdentifierRule;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import rule_nesting.iface.INestingRule;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

public class AlgoProxy implements LANG_STRUCT, EnumPOJOJoin {
    AlgoBase childAlgo;

    @Override
    public String getPushSymbol() {
        return childAlgo.getPushSymbol();
    }

    @Override
    public String getPopSymbol() {
        return childAlgo.getPopSymbol();
    }

    @Override
    public boolean go(IStackPayload stackTop) {
        return childAlgo.go(stackTop);
    }

    @Override
    public void onPush(IStackPayload stackTop) {
        childAlgo.onPush(stackTop);
    }

    @Override
    public void onPop(IStackPayload stackTop) {
        childAlgo.onPop(stackTop);
    }

    @Override
    public void onNest(IStackPayload newTop) {
        childAlgo.onNest(newTop);
    }

    @Override
    public void onRegainTop() {
        childAlgo.onRegainTop();
    }

    @Override
    public IIdentifierRule getIdentifierRule() {
        return childAlgo.getIdentifierRule();
    }

    @Override
    public INestingRule getNestingRule() {
        return childAlgo.getNestingRule();
    }

    @Override
    public IFollowRule getFollowRule() {
        return childAlgo.getFollowRule();
    }

    @Override
    public boolean doCoreTask(IStackPayload stackTop) {
        return childAlgo.doCoreTask(stackTop);
    }

    @Override
    public boolean codeBlockRequired() {
        return childAlgo.codeBlockRequired();
    }

    @Override
    public IStackPayload newStackPayload() {
        return childAlgo.newStackPayload();
    }

    /*=====EnumPOJOJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo, IAlgoStrategy[] pushes, IAlgoStrategy[] pops) {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
    }

    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo, IAlgoStrategy[] pushes, IAlgoStrategy[] pops) {
        childAlgo.initAlgo(parentEnum, null, pushes, pops);
        this.childAlgo = childAlgo;
        //((EnumPOJOJoin) this.algoProxy).initAlgo(this, childAlgo);
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        return childAlgo.getParentEnum();
    }

    @Override
    public LANG_STRUCT getChildAlgo() {
        return childAlgo.getChildAlgo();
    }
}
