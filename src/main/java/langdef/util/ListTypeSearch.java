package langdef.util;

import err.ERR_TYPE;
import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import langdef.LangConstants;
import langdef.STRUCT_LIST_TYPE;
import readnode.iface.IReadNode;

import java.util.ArrayList;
import java.util.List;

public class ListTypeSearch {
    private static ListTypeSearch instance;

    public static ListTypeSearch initInstance(){
        return (instance == null)? (instance = new ListTypeSearch()): instance;
    }

    private ListTypeSearch(){}

    private STRUCT_LIST_TYPE listTypeFound;
    private IGTreeNode treeNodeFound;
    private String[] pathFound;

    public STRUCT_LIST_TYPE listTypeFound(){
        return listTypeFound;
    }
    public IGTreeNode treeNodeFound(){
        return treeNodeFound;
    }
    public String[] pathFound(){
        return pathFound;
    }

    public boolean findById(String identifier) {
        IGTree<IReadNode> currTree;
        IGTreeNode currNode;
        for(STRUCT_LIST_TYPE currType : STRUCT_LIST_TYPE.values()){
            currTree = currType.getListTree();
            if((currNode = currTree.getParse().treeNodeFromId(currTree.getRoot(), identifier)) != null){
                treeNodeFound = currNode;
                listTypeFound = currType;
                return true;
            }
        }
        return false;
    }
    
    public ERR_TYPE treeNodeFromPartialPath(String... partialPath) {
        IGTree<IReadNode> currTree;
        IGTreeNode currNode;
        for(STRUCT_LIST_TYPE currType : STRUCT_LIST_TYPE.values()){
            currTree = currType.getListTree();
            IGTreeNode<IReadNode> root = currTree.getRoot();
            if(root != null && (currNode = currTree.getParse().treeNodeFromPartialPath(0, root, partialPath)) != null){
                treeNodeFound = currNode;
                listTypeFound = currType;
                return ERR_TYPE.NONE;
            }
        }
        return ERR_TYPE.INVALID_PATH;
    }

    public ERR_TYPE pathFromPartialPath(String... partialPath) {
        IGTree<IReadNode> currTree;
        String[] currPath;
        for(STRUCT_LIST_TYPE currType : STRUCT_LIST_TYPE.values()){
            currTree = currType.getListTree();
            IGTreeNode<IReadNode> root = currTree.getRoot();
            if(root != null && (currPath = currTree.getParse().pathFromPartialPath(root, partialPath)) != null){
                pathFound = currPath;
                listTypeFound = currType;
                return ERR_TYPE.NONE;
            }
        }
        return ERR_TYPE.INVALID_PATH;
    }
    public ERR_TYPE pathFromTreeNode(IGTreeNode<IReadNode> treeNode){
        IGTree<IReadNode> currTree;
        String[] currPath;
        for(STRUCT_LIST_TYPE currType : STRUCT_LIST_TYPE.values()){
            currTree = currType.getListTree();
            IGTreeNode<IReadNode> root = currTree.getRoot();
            if(root != null && (currPath = currTree.getParse().pathFromTreeNode(root, treeNode)) != null){
                pathFound = currPath;
                listTypeFound = currType;
                return ERR_TYPE.NONE;
            }
        }
        return ERR_TYPE.INVALID_PATH;
    }
    public boolean isPathToLeaf(String... partialPath){
        IGTree<IReadNode> currTree;
        for(STRUCT_LIST_TYPE currType : STRUCT_LIST_TYPE.values()){
            currTree = currType.getListTree();
            IGTreeNode<IReadNode> root = currTree.getRoot();
            if(root != null && currTree.getParse().isPathToLeaf(root, partialPath)){
                return true;
            }
        }
        return false;
    }

    public List<String> getAllPaths() {
        List<String> allPaths = new ArrayList<>();
        IGTree<IReadNode> currTree;
        for(STRUCT_LIST_TYPE currType : STRUCT_LIST_TYPE.values()){
            currTree = currType.getListTree();
            allPaths.addAll(currTree.getParse().getAllPaths(currTree.getRoot(), LangConstants.PATH_TREE_SEP));
        }
        return allPaths;
    }
}
