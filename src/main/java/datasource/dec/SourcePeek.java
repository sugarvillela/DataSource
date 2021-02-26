package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

/** Output is two iterations behind internal state.
 *  This should be among the outermost decorators */
public class SourcePeek extends DecoratorBase {
    private final IReadNode[] nodes;

    public SourcePeek(IDataSource dataSource) {
        super(dataSource);
        nodes = new IReadNode[3];
        if(this.hasData()){
            this.next();
            this.next();
        }
    }

    private void push(IReadNode newNode){
        int i;
        for(i = 0; i < nodes.length - 1; i ++){
            nodes[i] = nodes[i + 1];
        }
        nodes[i] = newNode;
    }

    @Override
    public boolean hasPeekBack() {
        return nodes[0] != null;
    }

    @Override
    public boolean hasNext() {
        return nodes[1] != null;
    }

    @Override
    public boolean hasPeekAhead() {
        return nodes[2] != null;
    }

    @Override
    public IReadNode peekBack() {
        return nodes[0];
    }

    @Override
    public IReadNode next() {
        this.push(dataSource.next());
        return nodes[0];
    }

    @Override
    public IReadNode peekAhead() {
        return nodes[2];
    }
}
