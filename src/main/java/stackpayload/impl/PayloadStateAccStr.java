package stackpayload.impl;

import java.util.ArrayList;

public class PayloadStateAccStr extends PayloadState {
    private final ArrayList<String> acc;

    public PayloadStateAccStr() {
        super();
        this.acc = new ArrayList<>();
    }

    @Override
    public void set(String string) {
        acc.add(string);
    }

    @Override
    public String getString() {
        if(acc.isEmpty()){
            return "";
        }
        else{
            String string = String.join(" ", acc);  // reconstruct line
            acc.clear();                                    // clear for next
            return string;
        }
    }

    @Override
    public String toString() {
        return "PayloadState{" +
                ", timeOnStack=" + timeOnStack +
                ", pushedReadNode=" + ((pushedReadNode == null)? "null" : pushedReadNode.text()) +
                ", acc='" + String.join(" ", acc) + '\'' +
                '}';
    }
}
