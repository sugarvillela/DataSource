package langdefsubalgo.impl;

import langdefsub.PAR_TYPE;
import langdefsub.PRIM_TYPE;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunList;
import langdefsubalgo.iface.IPatternFactory;
import runstate.Glob;

import java.util.ArrayList;
import java.util.List;

public class FunList implements IFunList {
    private final ArrayList<IFun> funList;

    public FunList(String text, IPatternFactory factory){
        //System.out.println("RxFunList: " + text);
        funList = factory.getFunBuildUtil().build(text);
        Glob.VALID_FUN_LIST.validatePrimTypeChain(funList);
        Glob.VALID_FUN_LIST.validateFunTypeChain(funList);
        Glob.VALID_FUN_LIST.validateRangeBeforeInChain(funList);
        System.out.println("List");
        for(IFun fun : funList){
            System.out.println(fun.description());
        }
    }

    @Override
    public PRIM_TYPE outType() {
        return funList.get(funList.size() - 1).primTypeAfter();
    }

    @Override
    public PAR_TYPE[] accessParamTypes() {
        return funList.get(0).parTypesBefore();
    }

    @Override
    public List<IFun> toList() {
        return funList;
    }

    @Override
    public String toString() {
        ArrayList<String> stringList = new ArrayList<>(funList.size());
        for(IFun fun : funList){
            stringList.add(fun.toString());
        }
        return String.join("\n", stringList);
    }
}
