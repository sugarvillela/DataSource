package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

public abstract class DecoratorBase implements IDataSource {
    protected final IDataSource dataSource;

    public DecoratorBase(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean hasData() {
        return dataSource != null && dataSource.hasData();
    }

    @Override
    public boolean hasNext() {
        return dataSource.hasNext();
    }

    @Override
    public boolean hasPeekBack() {
        return dataSource.hasPeekBack();
    }

    @Override
    public boolean hasPeekAhead() {
        return dataSource.hasPeekAhead();
    }

    @Override
    public IReadNode next() {
        return dataSource.next();
    }

    @Override
    public IReadNode peekBack() {
        return dataSource.peekBack();
    }

    @Override
    public IReadNode peekAhead() {
        return dataSource.next();
    }
}
