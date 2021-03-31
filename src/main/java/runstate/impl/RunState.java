package runstate.impl;

import datasource.core.SourceFile;
import datasource.dec.*;
import datasource.dec_fluid.SourceFluid;
import datasource.dec_tok.SourceTok;
import langdef.RulesByStructType_Follow;
import langdef.RulesByStructType_Nesting;
import langdef.RulesByStructType_Pop;
import langdefalgo.impl.AlgoImplGroupStep1;
import langdefalgo.impl.AlgoImplGroupStep2;
import readnode.iface.IReadNode;
import runstate.iface.IRunState;
import runstate.iface.IRunStep;
import stack.iface.IStackLog;
import stack.iface.IStructStack;
import stackpayload.iface.IStackPayload;

public class RunState implements IRunState {
    private static RunState instance;

    public static RunState initInstance(){
        return (instance == null)? (instance = new RunState()): instance;
    }

    private RunState(){}

    private String filePath, currLineText;
    private IRunStep currentSourceStep;


    @Override
    public void setInFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public IRunStep currentSourceStep() {
        return currentSourceStep;
    }

    /* Program state hooks */

    @Override
    public void initRunState() {
        new RulesByStructType_Nesting().initRules();
        new RulesByStructType_Follow().initRules();
        new RulesByStructType_Pop().initRules();

//        ArrayList<EnumPOJOJoin> enumPOJOJoins = Glob.ENUMS_BY_TYPE.allEnumAlgoJoin();
//        for(EnumPOJOJoin enumPOJOJoin : enumPOJOJoins){
//            enumPOJOJoin.initAlgo(null, null);
//        }
    }

    @Override
    public void initStep1() {
        new AlgoImplGroupStep1().initAlgos();

        currentSourceStep = new RunStep(
            //new SourceFirstAndLast(
                new SourceActiveOnly(
                    new SourceFluid(
                        new SourceNonComment(
                            new SourceTextPattern(
                                new SourceTok(
                                    new SourceNonEmpty(
                                        new SourceFile(filePath)
                                    )
                                )
                            )
                        )
                    )
                )
            //)
        );
    }

    @Override
    public void runPreScan() {

    }

    /*=====SourceStep methods, delegate to currSourceStep=============================================================*/

    @Override
    public void go() {
        currentSourceStep.go();
    }

    @Override
    public IReadNode getCurrNode() {
        return currentSourceStep.getCurrNode();
    }

    @Override
    public void goBack(IReadNode backNode) {
        currentSourceStep.goBack(backNode);
    }

    @Override
    public IStructStack getStack() {
        return currentSourceStep.getStack();
    }

    @Override
    public void setCurrNode(IReadNode newNode) {
        currentSourceStep.setCurrNode(newNode);
    }

    @Override
    public void push(IStackPayload stackPayload) {
        currentSourceStep.push(stackPayload);
    }

    @Override
    public void pop() {
        currentSourceStep.pop();
    }

    @Override
    public void popMost() {
        currentSourceStep.popMost();
    }

    @Override
    public IStackPayload top() {
        return currentSourceStep.top();
    }

    @Override
    public int size() {
        return currentSourceStep.size();
    }

    @Override
    public IStackLog getStackLog() {
        return currentSourceStep.getStackLog();
    }
}
