package stack.impl;

import stack.iface.IStackLog;
import stack.iface.IStructStack;
import stackpayload.iface.IStackPayload;

import java.util.Stack;

public class StructStack implements IStructStack {
    private final Stack<IStackPayload> stack;
    private final IStackLog stackLog;

    public StructStack() {
        stack = new Stack<>();
        this.stackLog = new StackLog();
    }

    @Override
    public void push(IStackPayload stackPayload) {
        //System.out.println("StructStack push: " + stackPayload.getLangStructEnum().toString());
        if(!stack.isEmpty()){

            stackPayload.setBelow(stack.peek());
//            stack.peek().getState().onPushAbove();
//            stack.peek().getState().onPushAdjAbove();
        }
        stack.push(stackPayload);
        stackPayload.onPush();
        stackPayload.addToStackLog(stackLog, null);
    }

    @Override
    public void pop() {
        if(!stack.isEmpty()){
            stack.peek().onPop();
            stack.pop();
            if(!stack.isEmpty()){
                stack.peek().addToStackLog(stackLog, null);
                stack.peek().getLangStructEnum().onRegainTop();
            }
        }
    }

    @Override
    public void popMost() {
        while(this.size() > 1){
            this.pop();
        }
    }

    @Override
    public IStackPayload top() {
        return stack.isEmpty()? null : stack.peek();
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public IStackLog getStackLog() {
        return stackLog;
    }
}
