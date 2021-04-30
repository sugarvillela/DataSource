package generictree.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.parse.GTreeParse;
import generictree.task.TaskToList;

import java.util.ArrayList;
import java.util.List;

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
    public boolean put(String... path) {
        return this.put(null, path);
    }

    @Override
    public void clear() {
        if(root != null){
            List<IGTreeNode<T>> list = new ArrayList<>();
            IGTreeTask<T> task = new TaskToList<T>(list);
            this.getParse().breadthFirst(this.getRoot(), task);
            for(int i = list.size() -1; i >= 0; i--){
                IGTreeNode<T> currNode = list.get(i);
                //System.out.println(currNode.friendlyString());
                if(!currNode.isLeaf()){
                    //System.out.println("clear");
                    currNode.getChildren().clear();
                }
            }
            root = null;
        }
    }

    @Override
    public IGTreeParse<T> getParse() {
        return parseObject;
    }

    protected String[] tokenizePathOnSingle(char splitChar, String... path){
        String[] tok;
        if(path.length == 1 && (tok = path[0].split("[" + splitChar + "]")).length > 1){
            return tok;
        }
        return path;
    }
}
