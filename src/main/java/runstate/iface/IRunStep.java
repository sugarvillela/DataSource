package runstate.iface;

import readnode.iface.IReadNode;
import stack.iface.IStructStack;

public interface IRunStep {//  extends IStructStack
    void go();

    IReadNode getCurrNode();

    IStructStack getStack();

}
