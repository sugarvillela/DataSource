package datasource.core;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

/** Most inner implementations inherit from this class, while most decorator implementations
 *  inherit from DecoratorBase.
 *  Any class that inherits from this can support peekBack and peekForward without a peek decorator,
 *  as long as no other decorator is applied (decorators take internal and external state out of sync) */
public abstract class DataSourceBase implements IDataSource {
    private static int uqValue = 0;
    protected final int numericIdentifier;  // for impl without fileName, uq prefix allows to tell sources apart
    protected String stringIdentifier;      // short file name, or just 'array' or 'list'

    protected int row;

    public DataSourceBase() {
        //this.numericIdentifier = uqValue++; // comment out for tests
        this.numericIdentifier = uqValue; // uncomment for tests
        this.row = -1;
    }

    // need adaptor for array and list impl
    protected abstract int dataSize();
    protected abstract IReadNode getData(int index);

    protected DataSourceBase tick(){
        row ++;
        return this;
    }

    protected String getIdentifier() {
        return stringIdentifier + "@" + numericIdentifier;
    }

    @Override
    public boolean hasNext() {
        return row < this.dataSize() - 1;
    }

    @Override
    public IReadNode next() {
        return this.tick().getData(row);
    }

    @Override
    public boolean hasPeekBack() {
        return row >= 0;
    }

    @Override
    public boolean hasPeekAhead() {
        return row < this.dataSize() -2;
    }

    @Override
    public IReadNode peekBack() {
        return this.getData(row - 1);
    }

    @Override
    public IReadNode peekAhead() {
        return this.getData(row + 1);
    }
}
