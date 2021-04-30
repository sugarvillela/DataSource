package generictree.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.task.TaskDisp;
import generictree.task.TaskToListLeaves;
import generictree.task.TaskToList;
import langdef.LangConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

class GTreeTest {
    private static class Payload {
        public final String text;

        public Payload(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
    @Test
    void initialTests() {
        IGTree<Payload> tree = new PathTree<>(LangConstants.PATH_TREE_SEP);
        tree.put(new Payload("payload0"), "id0");
        tree.put(new Payload("payload1"), "id0.id01");
        tree.put(new Payload("payload2"), "id0.id01.id02");
        tree.put(new Payload("payload3a"), "id0.id01.id02.id03a");
        tree.put(new Payload("payload3b"), "id0.id01.id02.id03b");
        tree.put(new Payload("payloadb"), "id0.id_b");
        tree.put(new Payload("payloadc0"), "id0.id_c0");
        tree.put(new Payload("payloadc1"), "id0.id_b.id_c1");
        ArrayList<IGTreeNode<Payload>> list = new ArrayList<>();

        tree.getParse().preOrder(tree.getRoot(), new TaskToList<>(list));
        for(IGTreeNode<Payload> node : list){
            System.out.println(node.friendlyString());
        }

        System.out.println("======");
        ArrayList<IGTreeNode<Payload>> leaves = new ArrayList<>();

        tree.getParse().preOrder(tree.getRoot(), new TaskToListLeaves<>(leaves));
        for(IGTreeNode<Payload> node : leaves){
            System.out.println(node.csvString());
        }

        System.out.println("======");
        IGTreeNode<Payload> found;
        if((found = tree.getParse().treeNodeFromId(tree.getRoot(), "id_c1")) != null){
            System.out.println("found: " + found.csvString());
        }
        else{
            System.out.println("nope");
        }
    }

    /*=====PathTree tests=============================================================================================*/

    private IGTree<Payload> mockPathTree(){
        IGTree<Payload> tree = new PathTree<>(LangConstants.PATH_TREE_SEP);
        tree.put(new Payload("payload0"), "id0");
        tree.put(new Payload("payload1"), "id0.id01");
        tree.put(new Payload("payload2"), "id0.id01.id02");
        tree.put(new Payload("payload3"), "id0.id01.id02.id03");
        tree.put(new Payload("payload4"), "id0.id01.id02.id03.id04");
        tree.put(new Payload("payload3b"), "id0.id01.id02.id03b");
        tree.put(new Payload("payloadb"), "id0.idb");
        tree.put(new Payload("payloadc0"), "id0.idc");
        tree.put(new Payload("payloadd"), "id0.idb.idd");

        return tree;
    }

    @Test
    void givenPopulatedTree_displayBreadthFirst() {
        IGTree<Payload> tree = mockPathTree();
        IGTreeTask<Payload> task = new TaskDisp<>();
        tree.getParse().breadthFirst(tree.getRoot(), task);
    }

    @Test
    void givenPopulatedTree_returnNodeForGivenIdentifier() {
        IGTree<Payload> tree = mockPathTree();
        String actual, expected;
        IGTreeNode<Payload> found;

        found = tree.getParse().treeNodeFromPartialPath(0, tree.getRoot(), "id01", "id02", "id03");
        expected = "(level: 3), bran, (children: 1), (op: -), (negated: -), (id: id03), (parent: id02), payload3";
        actual = (found == null)? "null" : found.friendlyString();
        System.out.println("friendlyString: " + actual);
        Assertions.assertEquals(expected, actual);

        found = tree.getParse().treeNodeFromPartialPath(0, tree.getRoot(), "id03");
        expected = "(level: 3), bran, (children: 1), (op: -), (negated: -), (id: id03), (parent: id02), payload3";
        actual = (found == null)? "null" : found.friendlyString();
        System.out.println("friendlyString: " + actual);
        Assertions.assertEquals(expected, actual);

        found = tree.getParse().treeNodeFromPartialPath(0, tree.getRoot(), "id0", "id01", "id02", "id03", "id04");
        expected = "(level: 4), leaf, (children: 0), (op: -), (negated: -), (id: id04), (parent: id03), payload4";
        actual = (found == null)? "null" : found.friendlyString();
        System.out.println("friendlyString: " + actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenPopulatedTree_returnFullPathForGivenIdentifier() {
        IGTree<Payload> tree = mockPathTree();
        String actual, expected;
        String[] path;

        path = tree.getParse().pathFromPartialPath(tree.getRoot(), "id02", "id03");
        expected = "id0, id01, id02, id03";
        actual = (path == null)? "null" : String.join(", ", path);
        System.out.println("pathString: " + actual);
        Assertions.assertEquals(expected, actual);

        path  = tree.getParse().pathFromPartialPath(tree.getRoot(), "id04");
        expected = "id0, id01, id02, id03, id04";
        actual = (path == null)? "null" : String.join(", ", path);
        System.out.println("pathString: " + actual);
        Assertions.assertEquals(expected, actual);

        path  = tree.getParse().pathFromPartialPath(tree.getRoot(), "idb");
        expected = "id0, idb";
        actual = (path == null)? "null" : String.join(", ", path);
        System.out.println("pathString: " + actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenPopulatedTree_returnAllPaths() {
        IGTree<Payload> tree = mockPathTree();
        IGTreeParse<Payload> parse = tree.getParse();
        List<String> paths = parse.getAllPaths(tree.getRoot(), '.');
        String actual = String.join("|", paths);
//        System.out.println("givenPopulatedTree_returnAllPaths");
//        for(String path : paths){
//            System.out.println("\"" + path + "|\" +");
//        }

        String expected = "id0.id01.id02.id03.id04|" +
                "id0.id01.id02.id03b|" +
                "id0.idb.idd|" +
                "id0.idc";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenPopulatedTree_clearAllRecursively() {
        IGTree<Payload> tree = mockPathTree();

        tree.getParse().preOrder(tree.getRoot(), new TaskDisp<>());
        System.out.println("====");
        tree.clear();
        System.out.println("====");
        //tree.getParse().preOrder(tree.getRoot(), new TaskDisp<>());

        Assertions.assertNull(tree.getRoot());
    }

    /*=====ParseTree tests============================================================================================*/

    @Test
    void givenFormattedString_populateParseTree() {
        IGTree<Payload> tree = new ParseTree<>();
        tree.put("zero|!(one&!two)|three");
        //tree.getRoot().csvString();
        ArrayList<IGTreeNode<Payload>> list = new ArrayList<>();
        tree.getParse().breadthFirst(tree.getRoot(), new TaskToList<>(list));

        ArrayList<String> actualList = new ArrayList<>();
        for(IGTreeNode<Payload> node : list){
            actualList.add(node.friendlyString());
            //System.out.println("\"" + node.friendlyString() + "|\" +");
        }
        String expected = "(level: 0), bran, (children: 3), (op: |), (negated: -), (id: root), (parent: -), -|" +
                "(level: 1), leaf, (children: 0), (op: -), (negated: -), (id: zero), (parent: root), -|" +
                "(level: 1), bran, (children: 2), (op: &), (negated: !), (id: &), (parent: root), -|" +
                "(level: 1), leaf, (children: 0), (op: -), (negated: -), (id: three), (parent: root), -|" +
                "(level: 2), leaf, (children: 0), (op: -), (negated: -), (id: one), (parent: &), -|" +
                "(level: 2), leaf, (children: 0), (op: -), (negated: !), (id: two), (parent: &), -";
        String actual = String.join("|", actualList);
        Assertions.assertEquals(expected, actual);
    }

    private IGTree<Payload> mockParseTree(){
        IGTree<Payload> tree = new ParseTree<>();
        tree.put("zero|!(one&two)|!three");
        // display?
        ArrayList<IGTreeNode<Payload>> list = new ArrayList<>();
        tree.getParse().preOrder(tree.getRoot(), new TaskToList<>(list));
        for(IGTreeNode<Payload> node : list){
            System.out.println(node.friendlyString());
        }
        return tree;
    }

    @Test
    void givenPopulatedTree_buildFormattedString() {// formatted for target language
        IGTree<Payload> tree = mockParseTree();

        String actual = tree.toString();
        String expected = "zero || !(one && two) || !three";
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }
}