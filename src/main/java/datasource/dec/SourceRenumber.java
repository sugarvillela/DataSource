package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

/** A decorator to renumber nodes being emitted (for reusing sorted and reinserted nodes) */
public class SourceRenumber extends DecoratorBase{

    public SourceRenumber(IDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public IReadNode next() {
        IReadNode currNode = dataSource.next();
        currNode.renumberSortValue();
        return currNode;
    }
}
