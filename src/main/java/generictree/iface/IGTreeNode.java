package generictree.iface;

import java.util.ArrayList;

public interface IGTreeNode <T> {
    String identifier();
    void setIdentifier(String identifier);
    boolean is(String identifier);

    void setPayload(T payload);
    T getPayload();

    void setLevel(int level);
    int level();

    void setParent(IGTreeNode <T> parent);
    IGTreeNode<T> parent();

    boolean isLeaf();

    ArrayList<IGTreeNode <T>> getChildren();

    void addChild(IGTreeNode <T> child);

    void addChild(String identifier, T payload);// add child of same subtype

    void setOp(char op);
    char op();

    void setNegated(boolean negated);
    boolean negated();

    void setWrapped(boolean wrapped);
    boolean wrapped();

    String friendlyString();

    String csvString();
}
