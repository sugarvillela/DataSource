package stack.iface;

import stackpayload.iface.IStackPayload;

public interface IStructStack {
    void push(IStackPayload newTop);
    void pop();
    void popMost();
    IStackPayload top();
    int size();
    IStackLog getStackLog();
}
