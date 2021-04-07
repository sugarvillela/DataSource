package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;
import readnode.iface.IReadNode;

public class TaskDispReadNode implements IGTreeTask<IReadNode> {
    @Override
    public boolean doTask(IGTreeNode<IReadNode> node) {
        System.out.println(node.friendlyString() + ": " + node.getPayload().csvString());
        return false;
    }
}
