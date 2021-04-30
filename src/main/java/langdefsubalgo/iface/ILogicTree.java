package langdefsubalgo.iface;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import langdefalgo.iface.LANG_STRUCT;

import java.util.List;

public interface ILogicTree {
    LANG_STRUCT langStruct();
    //void build();
    IGTree<IFunPattern> getTree();
    List<IGTreeNode<IFunPattern>> getLeaves();
}
