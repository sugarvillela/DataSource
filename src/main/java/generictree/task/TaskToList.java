package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;

import java.util.List;

public class TaskToList<T> implements IGTreeTask <T> {
    private final List<IGTreeNode<T>> list;

    /** Pass task to GTreeParse for breadth- or depth-first placement
     * @param list non-null, empty list, results appear in list on completion */
    public TaskToList(List<IGTreeNode<T>> list) {
        this.list = list;
    }

    @Override
    public boolean doTask(IGTreeNode<T> node) {
        list.add(node);
        return false;
    }
}
