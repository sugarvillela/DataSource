package langdefalgo.impl;

import err.ERR_TYPE;
import langdef.CMD;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import rule_follow.iface.IFollowRule;
import rule_nesting.iface.INestingRule;
import rule_pop.iface.IPopRule;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import stack.iface.IStackLogIterationItem;
import stackpayload.iface.IStackPayload;
import stackpayload.impl.StackPayload;
import rule_identifier.iface.IIdentifierRule;
import textevent.impl.TextEventNode;

public abstract class AlgoBase implements LANG_STRUCT, EnumPOJOJoin {
    protected LANG_STRUCT parentEnum;

    protected AlgoBase() {
    }

    protected abstract boolean doCoreTask(IStackPayload stackTop);

    // utility to add an 'add_to' command to each non-push, non-pop node
    protected void eventToCurrNode_addTo(){
        IReadNode currNode = Glob.RUN_STATE.getCurrNode();
        currNode.setTextEvent(new TextEventNode(parentEnum, CMD.ADD_TO, currNode.text()));
    }

    @Override
    public boolean go(IStackPayload stackTop) {
        return Glob.PUSH_POP_UTIL.handleTextEvent(stackTop) || doCoreTask(stackTop);
    }


    protected void onPush_checkIdentifierRule(IStackPayload stackTop){
        IReadNode pushReadNode = Glob.RUN_STATE.getCurrNode();
        IIdentifierRule identifierRule = parentEnum.getIdentifierRule();
        if(identifierRule.onPush(pushReadNode)){// start listening to identifier, if allowed
            stackTop.getState().setPushedIdentifier(identifierRule.getPushedIdentifier());
        }
    }
    protected void onPush_checkFollowRule(IStackPayload stackTop){
        IFollowRule followrule = parentEnum.getFollowRule();
        if(!followrule.allAreAllowed()){
            int stackLevel = Glob.RUN_STATE.size() - 1;
            IStackLogIterationItem prevItem;
            if(
                (prevItem = Glob.RUN_STATE.getStackLog().lastIterationItem(stackLevel)) == null ||
                !followrule.isAllowedPrev(prevItem.langStruct())
            ){
                Glob.ERR.kill(ERR_TYPE.SYNTAX);
            }
        }
    }
    protected void onPush_putPushNode(){
        Glob.DATA_SINK.put();// push node
    }

    @Override
    public void onPush(IStackPayload stackTop) {
        //System.out.println("AlgoBase push: " + this.getParentEnum());
        this.onPush_checkIdentifierRule(stackTop);
        this.onPush_checkFollowRule(stackTop);
        this.onPush_putPushNode();
    }

    protected void onPop_checkIdentifierRule(IStackPayload stackTop){
        String pushedIdentifier = stackTop.getState().getPushedIdentifier();// stop listening to identifier
        parentEnum.getIdentifierRule().onPop(pushedIdentifier);
    }
    protected void onPop_putPopNode(){
        IReadNode currNode = Glob.RUN_STATE.getCurrNode();
        IReadNode popNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(parentEnum, CMD.POP)).build();
        Glob.DATA_SINK.put(popNode);// pop node
    }
    @Override
    public void onPop(IStackPayload stackTop) {
        //System.out.println("AlgoBase pop: " + this.getParentEnum());
        this.onPop_putPopNode();
        this.onPop_checkIdentifierRule(stackTop);
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

}
