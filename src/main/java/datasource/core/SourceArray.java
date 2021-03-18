package datasource.core;

import readnode.iface.IReadNode;
import readnode.impl.ReadNode;

public class SourceArray extends DataSourceBase {
    private final String[] array;

    public SourceArray(String[] array) {
        this.array = array;
        this.stringIdentifier = "array";
    }

    @Override
    public boolean hasData() {
        return array != null && array.length != 0;
    }

    @Override
    protected int dataSize() {
        return array.length;
    }

    @Override
    protected IReadNode getData(int index) {
        if(index >= array.length){
            return null;
        }
        return new ReadNode(this.sourceName(), row, array[index], this.hasNext());
    }
}
