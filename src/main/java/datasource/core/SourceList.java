package datasource.core;

import readnode.iface.IReadNode;
import readnode.impl.ReadNode;

import java.util.List;

public class SourceList extends DataSourceBase{
    private final List<String> list;

    public SourceList(List<String> list) {
        this.list = list;
//        for(String item : list){
//            System.out.println(item);
//        }
        this.stringIdentifier = "list";
    }

    @Override
    public boolean hasData() {
        return list != null && !list.isEmpty();
    }

    @Override
    protected int dataSize() {
        return list.size();
    }

    @Override
    protected IReadNode getData(int index) {
        if(index >= list.size()){
            return null;
        }
        return new ReadNode(this.sourceName(), row, list.get(index), this.hasNext());
    }
}
