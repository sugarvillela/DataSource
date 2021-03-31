package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;

import java.util.List;

public class TaskLeavesToList<T> implements IGTreeTask<T> {
    private final List<IGTreeNode<T>> list;

    /** Pass task to GTreeParse; breadth- or depth-first doesn't matter
     * @param list non-null, empty list, results appear in list on completion */
    public TaskLeavesToList(List<IGTreeNode<T>> list) {
        this.list = list;
    }

    @Override
    public boolean doTask(IGTreeNode<T> node) {
        if(node.isLeaf()){
            list.add(node);
        }
        return false;
    }
}
