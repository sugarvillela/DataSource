package langdefsubalgo.implrx;

import langdefsub.PRIM_TYPE;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunList;
import langdefsubalgo.factory.FunBuildUtil;
import runstate.Glob;

import java.util.ArrayList;
import java.util.List;

public class RxFunList implements IFunList {
    private final ArrayList<IFun> funList;
    private final PRIM_TYPE outType;

    public RxFunList(String text){
        //System.out.println("RxFunList: " + text);
        funList = FunBuildUtil.initInstance().buildList(text);
        Glob.VALID_FUN_LIST.validateOutInMatch(funList);
        outType = funList.get(funList.size() - 1).outType();
//        System.out.println("List");
//        for(IRxFun fun : funList){
//            System.out.println(fun.description());
//        }
    }


    @Override
    public PRIM_TYPE outType() {
        return outType;
    }

    @Override
    public List<IFun> toList() {
        return funList;
    }
}
