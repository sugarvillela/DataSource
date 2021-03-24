package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

/** Output is one iteration behind internal state, or more if additional items are skipped. */
public abstract class InterceptorBase extends DecoratorBase{
    protected IReadNode currNode, nextNode;

    public InterceptorBase(IDataSource dataSource) {
        super(dataSource);
        if(this.hasData()){
            this.skip();
        }
    }

    /** Expect non null nextNode
     * @return true on per-impl skip criteria met */
    protected abstract boolean shouldSkip();

    protected void skip(){
        currNode = nextNode;
        do{
            nextNode = dataSource.next();
//            String csv = (nextNode == null)? "null" : nextNode.csvString() + " empty: " + (nextNode.text().trim().isEmpty());
//            System.out.println(this.getClass().getSimpleName() + ": " + csv);
        }
        while(nextNode != null && shouldSkip());
    }

    @Override
    public boolean hasNext() {
        return nextNode != null;
    }

    @Override
    public IReadNode next() {
        skip();
        return currNode;
    }
}
