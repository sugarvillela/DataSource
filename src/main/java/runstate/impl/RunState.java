package runstate.impl;

import datasource.core.SourceFile;
import datasource.dec.*;
import datasource.dec_fluid.SourceAccess;
import datasource.dec_fluid.SourceFluid;
import datasource.dec_tok.SourceTok;
import datasource.dec_tok.SourceTokSpecial;
import datasource.iface.IDataSource;
import langdef.RulesByStructType_Follow;
import langdef.RulesByStructType_Nesting;
import langdef.TokSpecialPatternInit;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoImplGroupStep1;
import langdefalgo.impl.AlgoImplGroupStep2;
import readnode.iface.IReadNode;
import runstate.Glob;
import runstate.iface.IRunState;
import runstate.iface.IRunStep;
import stack.iface.IStructStack;

import java.util.ArrayList;

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
        //new RulesByStructType_Pop().initRules();
        new TokSpecialPatternInit().initPatterns();

//        ArrayList<EnumPOJOJoin> enumPOJOJoins = Glob.ENUMS_BY_TYPE.allEnumAlgoJoin();
//        for(EnumPOJOJoin enumPOJOJoin : enumPOJOJoins){
//            enumPOJOJoin.initAlgo(null, null);
//        }
    }

    @Override
    public void initStep1() {
        new AlgoImplGroupStep1().initAlgos();

        currentSourceStep = new RunStep1(
            new SourceActiveOnly(
                new SourceFluid(
                    new SourceNonComment(
                        new SourceTextEvent(
                            new SourceTokSpecial(
                                new SourceTok(
                                    new SourceNonEmpty(
                                        new SourceFile(filePath)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    @Override
    public void initStep2() {
        new AlgoImplGroupStep2().initAlgos();
        IDataSource outputStep1 =
                Glob.DATA_SINK.getIdentifier(Glob.ENUMS_BY_TYPE.enumLangRoot1().toString()).toDataSource();

        currentSourceStep = new RunStep2(
            new SourceActiveOnly(
                new SourceAccess(
                        outputStep1
                )
            )
        );
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
    public IStructStack getStack() {
        return currentSourceStep.getStack();
    }

}
