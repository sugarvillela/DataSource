package langdefalgo.impl;

import langdef.CMD;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import nestingrule.iface.INestingRule;
import pushpoputil.iface.IPopRule;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;
import stackpayload.impl.StackPayload;
import IdenfifierRule.iface.IIdentifierRule;
import textevent.impl.TextEventNode;

public abstract class AlgoBase implements LANG_STRUCT, EnumPOJOJoin {
    protected LANG_STRUCT parentEnum;

    protected AlgoBase() {
    }

    protected abstract boolean doCoreTask(IStackPayload stackPayload);

    // utility to add an 'add_to' command to each non-push, non-pop node
    protected void eventToCurrNode_addTo(){
        IReadNode currNode = Glob.RUN_STATE.getCurrNode();
        currNode.setTextEvent(new TextEventNode(parentEnum, CMD.ADD_TO, currNode.text()));
    }

    @Override
    public boolean go(IStackPayload stackPayload) {
        return Glob.PUSH_POP_UTIL.handleTextEvent(stackPayload) || doCoreTask(stackPayload);
    }

    protected void onPush_putPushNode(){
        Glob.DATA_SINK.put();// push node
    }
    protected void onPush_checkIdentifierRule(IStackPayload stackPayload){
        IReadNode pushReadNode = Glob.RUN_STATE.getCurrNode();
        if(parentEnum.getIdentifierRule().onPush(pushReadNode)){// start listening to identifier, if allowed
            stackPayload.getState().setPushedReadNode(pushReadNode);
        }
    }
    @Override
    public void onPush(IStackPayload stackPayload) {
        //System.out.println("AlgoBase push: " + this.getParentEnum());
        this.onPush_putPushNode();
        this.onPush_checkIdentifierRule(stackPayload);
    }

    protected void onPop_checkIdentifierRule(IStackPayload stackPayload){
        IReadNode pushReadNode = stackPayload.getState().getPushedReadNode();// stop listening to identifier
        parentEnum.getIdentifierRule().onPop(pushReadNode);
    }
    protected void onPop_putPopNode(){
        IReadNode currNode = Glob.RUN_STATE.getCurrNode();
        IReadNode popNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(parentEnum, CMD.POP)).build();
        Glob.DATA_SINK.put(popNode);// pop node
    }
    @Override
    public void onPop(IStackPayload stackPayload) {
        System.out.println("AlgoBase pop: " + this.getParentEnum());
        this.onPop_checkIdentifierRule(stackPayload);
        this.onPop_putPopNode();
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
    public IPopRule getPopRule() {
        return parentEnum.getPopRule();
    }

    @Override
    public IStackPayload newStackPayload() {
        return new StackPayload(this.parentEnum);
    }

    /*=====EnumAlgoJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo) {
        Glob.ERR_DEV.kill("Call one-arg initAlgo() only on parent enum");
    }
    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo) {
        this.parentEnum = parentEnum;
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

    @Override
    public boolean isSelfPop() {
        Glob.ERR_DEV.kill("This should be called on the parent enum");
        return false;
    }
}
