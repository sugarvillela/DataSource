package langdefalgo.impl;

import IdenfifierRule.iface.IIdentifierRule;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import nestingrule.iface.INestingRule;
import pushpoputil.iface.IPopRule;
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
    public boolean isSelfPop() {
        return childAlgo.isSelfPop();
    }

    @Override
    public boolean go(IStackPayload stackPayload) {
        return childAlgo.go(stackPayload);
    }

    @Override
    public void onPush(IStackPayload stackPayload) {
        childAlgo.onPush(stackPayload);
    }

    @Override
    public void onPop(IStackPayload stackPayload) {
        childAlgo.onPop(stackPayload);
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
    public IPopRule getPopRule() {
        return childAlgo.getPopRule();
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
