package generictree.iface;

public interface IGTreeTask <T> {
    boolean doTask(IGTreeNode<T> node);
}
