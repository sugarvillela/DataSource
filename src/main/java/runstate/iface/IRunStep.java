package runstate.iface;

import readnode.iface.IReadNode;
import stack.iface.IStructStack;

public interface IRunStep extends IStructStack {
    void go();

    void setCurrNode(IReadNode newNode);
    IReadNode getCurrNode();

    void goBack(IReadNode backNode);

    IStructStack getStack();

}
