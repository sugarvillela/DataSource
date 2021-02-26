package datasource.core;

import readnode.iface.IReadNode;

/** Simple iterator for nodes that already exist on array */
public class SourceNode extends DataSourceBase {
    private final IReadNode[] nodes;

    public SourceNode(IReadNode[] nodes) {
        this.nodes = nodes;
    }

    @Override
    protected int dataSize() {
        return nodes.length;
    }

    @Override
    protected IReadNode getData(int index) {
        return (index >= nodes.length)? null : nodes[index];
    }

    @Override
    public boolean hasData() {
        return nodes != null && nodes.length > 0;
    }
}
