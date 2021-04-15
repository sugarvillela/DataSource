package runstate.iface;

import readnode.iface.IReadNode;
import stack.iface.IStructStack;

public interface IRunStep {//  extends IStructStack
    void go();

    void setCurrNode(IReadNode currNode);
    IReadNode getCurrNode();

    IStructStack getStack();

}
