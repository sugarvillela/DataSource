package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;

public class TaskDisp<T> implements IGTreeTask<T> {
    @Override
    public boolean doTask(IGTreeNode<T> node) {
        System.out.println(node.friendlyString());
        return false;
    }
}
