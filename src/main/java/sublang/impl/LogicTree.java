package sublang.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.impl.ParseTree;
import generictree.task.TaskDisp;
import generictree.task.TaskToListLeaves;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import runstate.Glob;
import sublang.iface.ILogicTree;
import sublang.iface.ILogicTreeNode;
import sublang.iface.IRxFunPattern;

import java.util.ArrayList;
import java.util.List;

public class LogicTree implements ILogicTree {
    private final LANG_STRUCT langStruct;
    private final IGTree<ILogicTreeNode> parseTree;

    public LogicTree() {
        IReadNode currNode = Glob.RUN_STATE.getCurrNode();
        System.out.println(currNode.csvString());

        this.langStruct = currNode.textEvent().langStruct();
        parseTree = new ParseTree<>();
        this.populate(currNode.text());
    }

    private void populate(String text){
        parseTree.put(text);
        List<IGTreeNode<ILogicTreeNode>> leaves = this.getLeaves();
        for(IGTreeNode<ILogicTreeNode> leaf: leaves){
            IRxFunPattern funPattern = new RxFunPattern(leaf.identifier());
        }
    }

    private List<IGTreeNode<ILogicTreeNode>> getLeaves(){
        List<IGTreeNode<ILogicTreeNode>> leaves = new ArrayList<>();
        parseTree.getParse().preOrder(parseTree.getRoot(), new TaskToListLeaves<>(leaves));
        return leaves;
    }

    @Override
    public LANG_STRUCT langStruct() {
        return langStruct;
    }

    @Override
    public IGTree<ILogicTreeNode> getTree() {
        return parseTree;
    }
}
