package datasource.core;

import readnode.iface.IReadNode;

import java.util.ArrayList;

public class SourceNodeList extends DataSourceBase {
    private final ArrayList<IReadNode> nodes;

    public SourceNodeList(ArrayList<IReadNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    protected int dataSize() {
        return nodes.size();
    }

    @Override
    protected IReadNode getData(int index) {
        return (index >= nodes.size())? null : nodes.get(index);
    }

    @Override
    public boolean hasData() {
        return nodes != null && nodes.size() > 0;
    }
}
