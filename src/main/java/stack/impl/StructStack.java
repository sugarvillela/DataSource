package stack.impl;

import stack.iface.IStructStack;
import stackpayload.iface.IStackPayload;

import java.util.Stack;

public class StructStack implements IStructStack {
    private final Stack<IStackPayload> stack;

    public StructStack() {
        stack = new Stack<>();
    }

    @Override
    public void push(IStackPayload stackPayload) {
        System.out.println("StructStack push: " + stackPayload.getLangStructEnum().toString());
        if(!stack.isEmpty()){
            stackPayload.setBelow(stack.peek());
//            stack.peek().getState().onPushAbove();
//            stack.peek().getState().onPushAdjAbove();
        }
        stack.push(stackPayload);
        stackPayload.onPush();
    }

    @Override
    public void pop() {
        if(!stack.isEmpty()){
            stack.peek().onPop();
            stack.pop();
            if(!stack.isEmpty()){
                stack.peek().getLangStructEnum().onRegainTop();
            }
        }
    }

    @Override
    public void popMost() {
        while(stack.size() > 1){
            //System.out.println("\npopMost 1:" + stack.peek().getLangStructEnum());
            stack.peek().onPop();
            stack.pop();
            //System.out.println("popMost 2:" + stack.peek().getLangStructEnum());
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
}
