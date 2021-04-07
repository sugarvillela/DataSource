package langdefalgo.impl;

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
    public boolean codeBlockRequired() {
        return childAlgo.codeBlockRequired();
    }

    @Override
    public IStackPayload newStackPayload() {
        return childAlgo.newStackPayload();
    }

    /*=====EnumPOJOJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo) {
        Glob.ERR_DEV.kill("Call one-arg initAlgo() only on parent enum");
    }

    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo) {
        childAlgo.initAlgo(parentEnum, null);
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
