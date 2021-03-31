package generictree.node;

import generictree.iface.IGTreeNode;

import java.util.ArrayList;

/** Salad bar pattern: take what you like and leave the rest.
 *
 * @param <T> the IGTreeNode payload type
 */
public abstract class GTreeNodeBase<T> implements IGTreeNode<T> {
    protected static final String NULL_STRING = "-";

    protected final ArrayList<IGTreeNode<T>> children;
    protected IGTreeNode<T> parent;
    protected T payload;
    protected String identifier;
    protected int level;

    public GTreeNodeBase() {
        children = new ArrayList<>();
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean is(String identifier) {
        return (this.identifier != null && this.identifier.equals(identifier));
    }

    @Override
    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int level() {
        return level;
    }

    @Override
    public void setParent(IGTreeNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public IGTreeNode<T> parent() {
        return parent;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public ArrayList<IGTreeNode<T>> getChildren() {
        return children;
    }


    @Override
    public void setOp(char op) {
        throw new IllegalStateException("Not implemented: use ParseTreeNode");
    }

    @Override
    public char op() {
        throw new IllegalStateException("Not implemented: use ParseTreeNode");
    }

    @Override
    public void setNegated(boolean negated) {
        throw new IllegalStateException("Not implemented: use ParseTreeNode");
    }

    @Override
    public boolean negated() {
        throw new IllegalStateException("Not implemented: use ParseTreeNode");
    }

    @Override
    public void setWrapped(boolean wrapped) {
        throw new IllegalStateException("Not implemented: use ParseTreeNode");
    }

    @Override
    public boolean wrapped() {
        throw new IllegalStateException("Not implemented: use ParseTreeNode");
    }
}
