package generictree.listsink;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.impl.PathTree;
import langdef.LangConstants;
import readnode.iface.IReadNode;
import runstate.Glob;

public class ListTree implements IGTree<IReadNode> {
    private final IGTree<IReadNode> tree;

    public ListTree() {
        tree =  new PathTree<>(LangConstants.PATH_TREE_SEP);
    }

    @Override
    public IGTreeNode<IReadNode> getRoot() {
        return tree.getRoot();
    }

    @Override
    public boolean put(String... path) {
        return tree.put(Glob.RUN_STATE.getCurrNode(), path);
    }

    @Override
    public boolean put(IReadNode payload, String... path) {
        return tree.put(payload, path);
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public IGTreeParse<IReadNode> getParse() {
        return tree.getParse();
    }
}
