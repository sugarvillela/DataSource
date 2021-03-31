package generictree.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.parse.GTreeParse;

public abstract class GTreeBase <T> implements IGTree<T> {
    protected final IGTreeParse<T> parseObject;
    protected IGTreeNode<T> root;

    public GTreeBase() {
        parseObject = new GTreeParse<>();
    }

    @Override
    public IGTreeNode<T> getRoot() {
        return root;
    }

    @Override
    public boolean put(String path) {
        return this.put(path, null);
    }

    @Override
    public IGTreeParse<T> getParse() {
        return parseObject;
    }
}
