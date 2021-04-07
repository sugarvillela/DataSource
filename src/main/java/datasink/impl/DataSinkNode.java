package datasink.impl;

import datasink.iface.IDataSinkNode;
import datasource.core.SourceNodeList;
import datasource.iface.IDataSource;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;

import java.util.ArrayList;

public class DataSinkNode implements IDataSinkNode {
    private final ArrayList<IReadNode> nodes;
    private final String identifier;
    private final LANG_STRUCT parentEnum;
    private boolean listening, skippedLast;

    public DataSinkNode(String identifier, LANG_STRUCT parentEnum) {
        this.identifier = identifier;
        this.parentEnum = parentEnum;
        nodes = new ArrayList<>();
        listening = true;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        return parentEnum;
    }

    @Override
    public void setListening(boolean listening) {
        this.listening = listening;
    }

    @Override
    public void put(IReadNode readNode) {
        if(listening){
            nodes.add(readNode);
            skippedLast = false;
//            System.out.println("=====dataSinkCore.put=====");
//            for(IReadNode node : nodes){
//                System.out.println(node.csvString());
//            }
//            System.out.println("==========");
        }
        else{
            skippedLast = true;
        }
    }

    @Override
    public IDataSource toDataSource() {
        ArrayList<IReadNode> out = new ArrayList<>(nodes);
        nodes.clear();
        return new SourceNodeList(out);
    }

    @Override
    public ArrayList<IReadNode> getNodes() {
        return nodes;
    }

}
