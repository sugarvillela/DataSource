package generictree.listsink;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.impl.PathTree;
import langdef.LangConstants;
import readnode.iface.IReadNode;
import runstate.Glob;

public class ListSink implements IGTree<IReadNode> {
    private final IGTree<IReadNode> tree;

    public ListSink() {
        tree =  new PathTree<>(LangConstants.PATH_TREE_SEP);
    }

    @Override
    public IGTreeNode<IReadNode> getRoot() {
        return tree.getRoot();
    }

    @Override
    public boolean put(String path) {
        return tree.put(path, Glob.RUN_STATE.getCurrNode());
    }

    @Override
    public boolean put(String path, IReadNode payload) {
        return tree.put(path, payload);
    }

    @Override
    public IGTreeParse<IReadNode> getParse() {
        return tree.getParse();
    }
}
