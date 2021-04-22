package langdefsubalgo.impl;

import langdefsubalgo.iface.IRxFun;

import java.util.ArrayList;

public class RxData  implements IRxFun {
    public RxData(ArrayList<String> components){
        System.out.println("RxData: " + String.join(".", components));
    }
}
