package langdefsubalgo.impl;

import langdefsubalgo.iface.IRxFun;
import langdefsubalgo.iface.IRxFunList;
import langdefsubalgo.unpack.FunFactory;

import java.util.ArrayList;

public class RxFunList implements IRxFunList {
    private final ArrayList<IRxFun> funList;

    public RxFunList(String text){
        funList = new FunFactory().buildList(text);
    }


    @Override
    public void validate() {// validate out to in

    }

    @Override
    public IRxFun[] toArray() {
        return null;
    }
}
