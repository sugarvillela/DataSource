package langdefsubalgo.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.impl.ParseTree;
import generictree.task.TaskToListLeaves;
import langdefalgo.iface.LANG_STRUCT;
import langdefsubalgo.factory.FactoryFx;
import langdefsubalgo.factory.FactoryRx;
import langdefsubalgo.iface.IPatternFactory;
import readnode.iface.IReadNode;
import langdefsubalgo.iface.ILogicTree;
import langdefsubalgo.iface.IFunPattern;

import java.util.ArrayList;
import java.util.List;

import static langdef.STRUCT_KEYWORD.RX;

public class LogicTree implements ILogicTree {
    private final LANG_STRUCT langStruct;
    private final IGTree<IFunPattern> parseTree;

    public LogicTree(IReadNode currNode) {
        System.out.println(currNode.csvString());

        this.langStruct = currNode.textEvent().langStruct();
        this.parseTree = new ParseTree<>();
        this.populate(currNode.text());
    }

    private void populate(String text){
        parseTree.put(text);
        List<IGTreeNode<IFunPattern>> leaves = this.getLeaves();

        IPatternFactory factory = (langStruct == RX)? FactoryRx.initInstance() : FactoryFx.initInstance();

        for(IGTreeNode<IFunPattern> leaf: leaves){
            String leafText = leaf.identifier();
            leaf.setPayload(
                    factory.newFunPattern(leafText)
            );
        }
    }

    @Override
    public LANG_STRUCT langStruct() {
        return langStruct;
    }

    @Override
    public IGTree<IFunPattern> getTree() {
        return parseTree;
    }

    @Override
    public List<IGTreeNode<IFunPattern>> getLeaves() {
        List<IGTreeNode<IFunPattern>> leaves = new ArrayList<>();
        parseTree.getParse().preOrder(parseTree.getRoot(), new TaskToListLeaves<>(leaves));
        return leaves;
    }
}
