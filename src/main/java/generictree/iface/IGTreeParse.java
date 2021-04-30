package generictree.iface;

import java.util.List;

public interface IGTreeParse <T> {
    IGTreeNode<T> treeNodeFromId(IGTreeNode<T> root, String identifier);

    /*====PathTree algorithms=========================================================================================*/

    IGTreeNode<T> treeNodeFromPartialPath(int index, IGTreeNode<T> root, String... partialPath);

    String[] pathFromPartialPath(IGTreeNode<T> root, String... partialPath);

    String[] pathFromTreeNode(IGTreeNode<T> root, IGTreeNode<T> treeNode);

    boolean isPathToLeaf(IGTreeNode<T> root, String... partialPath);

    boolean putByPath(T payload, int level, IGTreeNode<T> root, String... path);

    List<String> getAllPaths(IGTreeNode<T> root, char pathSep);

    /*====General tree parse algorithms===============================================================================*/

    boolean preOrder(IGTreeNode<T> root, IGTreeTask<T> task);

    boolean postOrder(IGTreeNode<T> root, IGTreeTask<T> task);

    boolean breadthFirst(IGTreeNode<T> root, IGTreeTask<T> task);

}
