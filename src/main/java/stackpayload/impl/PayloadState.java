package stackpayload.impl;

import readnode.iface.IReadNode;
import stackpayload.iface.IPayloadState;
import stackpayload.iface.IStackPayload;

public class PayloadState implements IPayloadState {
    protected IStackPayload parentPayload;
    protected IReadNode pushedReadNode;
    protected int intState, timeOnStack;//, maxAbove, maxAdjAbove;
    protected String stringState;

    public PayloadState() {}

    @Override
    public void init(IStackPayload parentPayload) {
        this.parentPayload = parentPayload;
    }

    @Override
    public void set(int state) {
        this.intState = state;
    }

    @Override
    public void set(String string) {
        this.stringState = string;
    }

    @Override
    public int getInt() {
        return intState;
    }

    @Override
    public String getString() {
        return stringState;
    }

    @Override
    public void setPushedReadNode(IReadNode pushedReadNode) {
        this.pushedReadNode = pushedReadNode;
    }

    @Override
    public IReadNode getPushedReadNode() {
        return pushedReadNode;
    }

    @Override
    public void incTimeOnStack() {
        timeOnStack++;
        IStackPayload below = parentPayload.getBelow();
        if(below != null){
            below.getState().incTimeOnStack();
        }
    }

    @Override
    public int timeOnStack() {
        return timeOnStack;
    }

    @Override
    public String toString() {
        return "PayloadState{" +
                ", timeOnStack=" + timeOnStack +
                ", pushedReadNode=" + ((pushedReadNode == null)? "null" : pushedReadNode.text()) +
                ", intState=" + intState +
                ", stringState='" + stringState + '\'' +
                '}';
    }

//    @Override
//    public void onPushAbove() {
//        maxAbove++;
//        IStackPayload below = parentPayload.getBelow();
//        if(below != null){
//            below.getState().onPushAbove();
//        }
//    }
//
//    @Override
//    public int getMaxAbove() {
//        return maxAbove;
//    }
//
//    @Override
//    public void onPushAdjAbove() {
//        maxAdjAbove++;
//    }
//
//    @Override
//    public int getMaxAdjAbove() {
//        return maxAdjAbove;
//    }
}
