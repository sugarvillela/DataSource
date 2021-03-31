package generictree.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;
import generictree.task.TaskDisp;
import generictree.task.TaskLeavesToList;
import generictree.task.TaskToList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GTreeTest {

    @Test
    void pathTree() {
        IGTree<String> tree = new PathTree<>('-');
        tree.put("id0", "payload0");
        tree.put("id0-id01", "payload1");
        tree.put("id0-id01-id02", "payload2");
        tree.put("id0-id01-id02-id03a", "payload3a");
        tree.put("id0-id01-id02-id03b", "payload3b");
        tree.put("id0-id_b", "payloadb");
        tree.put("id0-id_c0", "payloadc0");
        tree.put("id0-id_b-id_c1", "payloadc1");
        ArrayList<IGTreeNode<String>> list = new ArrayList<>();

        tree.getParse().preOrder(tree.getRoot(), new TaskToList<>(list));
        for(IGTreeNode<String> node : list){
            System.out.println(node.friendlyString());
        }

        System.out.println("======");
        ArrayList<IGTreeNode<String>> leaves = new ArrayList<>();

        tree.getParse().preOrder(tree.getRoot(), new TaskLeavesToList<>(leaves));
        for(IGTreeNode<String> node : leaves){
            System.out.println(node.csvString());
        }

        System.out.println("======");
        IGTreeNode<String> found;
        if((found = tree.getParse().findById(tree.getRoot(), "id_c1")) != null){
            System.out.println("found: " + found.csvString());
        }
        else{
            System.out.println("nope");
        }
    }
    private IGTree<String> mockPathTree(){
        IGTree<String> tree = new PathTree<>('-');
        tree.put("id0", "payload0");
        tree.put("id0-id01", "payload1");
        tree.put("id0-id01-id02", "payload2");
        tree.put("id0-id01-id02-id03", "payload3");
        tree.put("id0-id01-id02-id03-id04", "payload4");
        tree.put("id0-id01-id02-id03b", "payload3b");
        tree.put("id0-idb", "payloadb");
        tree.put("id0-idc", "payloadc");
        tree.put("id0-idb-idd", "payloadd");
        return tree;
    }
    @Test
    void breadthFirst() {
        IGTree<String> tree = mockPathTree();
        IGTreeTask<String> task = new TaskDisp<>();
        tree.getParse().breadthFirst(tree.getRoot(), task);
    }
    @Test
    void findByPath() {
        IGTree<String> tree = mockPathTree();
        IGTreeNode<String> found = tree.getParse().findByPartialPath(0, tree.getRoot(), "id01", "id02", "id03");
        String friendlyString = (found == null)? "null" : found.friendlyString();
        System.out.println("friendlyString: " + friendlyString);

        found = tree.getParse().findByPartialPath(0, tree.getRoot(), "id03");
        friendlyString = (found == null)? "null" : found.friendlyString();
        System.out.println("friendlyString: " + friendlyString);

        found = tree.getParse().findByPartialPath(0, tree.getRoot(), "id0", "id01", "id02", "id03", "id04");
        friendlyString = (found == null)? "null" : found.friendlyString();
        System.out.println("friendlyString: " + friendlyString);
    }
    @Test
    void fullPath() {
        IGTree<String> tree = mockPathTree();
        String[] path  = tree.getParse().getFullPath(tree.getRoot(), "id02", "id03");
        String pathString = (path == null)? "null" : String.join(", ", path);
        System.out.println("pathString: " + pathString);

        path  = tree.getParse().getFullPath(tree.getRoot(), "id04");
        pathString = (path == null)? "null" : String.join(", ", path);
        System.out.println("pathString: " + pathString);

        path  = tree.getParse().getFullPath(tree.getRoot(), "idb");
        pathString = (path == null)? "null" : String.join(", ", path);
        System.out.println("pathString: " + pathString);
    }
    @Test
    void splitTree() {
        IGTree<String> tree = new ParseTree<>();
        tree.put("zero|!(one&!two)|three");
        tree.getRoot().csvString();
        ArrayList<IGTreeNode<String>> list = new ArrayList<>();
        tree.getParse().preOrder(tree.getRoot(), new TaskToList<>(list));
        for(IGTreeNode<String> node : list){
            System.out.println(node.friendlyString());
        }
    }
    private IGTree<String> mockParseTree(){
        IGTree<String> tree = new ParseTree<>();
        tree.put("zero|!(one&two)|!three");
        // display?
        ArrayList<IGTreeNode<String>> list = new ArrayList<>();
        tree.getParse().preOrder(tree.getRoot(), new TaskToList<>(list));
        for(IGTreeNode<String> node : list){
            System.out.println(node.friendlyString());
        }
        return tree;
    }
    @Test
    void unParse() {
        IGTree<String> tree = mockParseTree();
        System.out.println(tree.toString());
    }
}