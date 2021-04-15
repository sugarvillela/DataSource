package sublang.iface;

import generictree.iface.IGTree;
import langdefalgo.iface.LANG_STRUCT;

public interface ILogicTree {
    LANG_STRUCT langStruct();
    //void build();
    IGTree<ILogicTreeNode> getTree();
}
