package langdefsubalgo.impl;

import langdefsubalgo.iface.IRxFun;

import java.util.ArrayList;

public class RxFun implements IRxFun {
    public RxFun(ArrayList<String> components){
        System.out.println("RxFun: " + String.join(".", components));
    }
}
