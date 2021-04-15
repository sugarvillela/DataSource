package generictree.parse;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GTreeParse<T> implements IGTreeParse<T> {
    @Override
    public IGTreeNode<T> findById(IGTreeNode<T> root, String identifier) {
        if(root.is(identifier)){
            return root;
        }
        else{
            IGTreeNode<T> foundNode;
            for(IGTreeNode<T> child : root.getChildren()){
                if((foundNode = findById(child, identifier)) != null){
                    return foundNode;
                }
            }
        }
        return null;
    }

    @Override
    public IGTreeNode<T> findByPartialPath(int index, IGTreeNode<T> root, String... partialPath) {
        if(index < partialPath.length){
            if(root.is(partialPath[index])){
                index++;
                if(index == partialPath.length){
                    return root;
                }
            }
            IGTreeNode<T> foundNode;
            for(IGTreeNode<T> child : root.getChildren()){
                if((foundNode = findByPartialPath(index, child, partialPath)) != null){
                    return foundNode;
                }
            }
        }
        return null;
    }

    @Override
    public String[] getFullPath(IGTreeNode<T> root, String... partialPath) {
        IGTreeNode<T> found = this.findByPartialPath(0, root, partialPath);
        if(found != null){
            String[] fullPath = new String[found.level() + 1];
            do{
                fullPath[found.level()] = found.identifier();
            }
            while((found = found.parent()) != null);
            return fullPath;
        }
        return null;
    }

    @Override
    public boolean putByPath(T payload, int level, IGTreeNode<T> root, String... path) {
        if(level < path.length - 1){
            if(level == path.length - 2){
                if(root.is(path[level])){
                    root.addChild(path[level + 1], payload);
                    return true;
                }
            }
            else{
                for(IGTreeNode<T> child : root.getChildren()){
                    if(child.is(path[level + 1])){
                        return putByPath(payload, level + 1, child, path);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> getAllPaths(IGTreeNode<T> root, char pathSep) {
        return new AllPathsUtil(root, pathSep).getPaths();
    }

    @Override
    public boolean preOrder(IGTreeNode<T> root, IGTreeTask<T> task) {
        boolean result = task.doTask(root);
        for(IGTreeNode<T> child : root.getChildren()){
            result |= preOrder(child, task);
        }
        return result;
    }

    @Override
    public boolean postOrder(IGTreeNode<T> root, IGTreeTask<T> task) {
        boolean result = false;
        for(IGTreeNode<T> child : root.getChildren()){
            result |= postOrder(child, task);
        }
        result |= task.doTask(root);
        return result;
    }

    @Override
    public boolean breadthFirst (IGTreeNode<T> root, IGTreeTask<T> task) {
        boolean result = false;
        final Queue<IGTreeNode<T>> queue = new ArrayDeque<>();

        queue.add(root);

        while(!queue.isEmpty()) {
            IGTreeNode<T> curr = queue.remove();
            result |= task.doTask(curr);
            if(!curr.isLeaf()){
                queue.addAll(curr.getChildren());
            }
        }

        return result;
    }

    private static class AllPathsUtil <T>{
        private final IGTreeNode<T> root;
        private final List<String> paths;
        private final char pathSep;

        AllPathsUtil(IGTreeNode<T> root, char pathSep){
            this.root = root;
            this.pathSep = pathSep;
            this.paths = new ArrayList<>();
            if(root != null){
                this.recurse(root, "");
            }
        }
        private void recurse(IGTreeNode<T> curr, String currPath) {
            if(curr.isLeaf()){
                paths.add(currPath + pathSep + curr.identifier());
            }
            else{
                for(IGTreeNode<T> child : curr.getChildren()){
                    String newPath = (curr.isRoot())?
                        curr.identifier() : currPath + pathSep + curr.identifier();
                    recurse(child, newPath);
                }
            }
        }
        public List<String> getPaths(){
            return paths;
        }
    }
}
