package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

/** Output is one iteration behind internal state, or more if blank lines are skipped. */
public class SourceNonEmpty extends DecoratorBase{
    private IReadNode currNode, nextNode;
    private boolean more;

    public SourceNonEmpty(IDataSource dataSource) {
        super(dataSource);
        if(this.hasData()){
            more = true;
            this.skipSpaces();
        }
        else{
            more = false;
        }
    }

    private void skipSpaces(){
        currNode = nextNode;
        if(more){
            do{
                nextNode = dataSource.next();
            }
            while(nextNode != null && nextNode.text().isEmpty());
        }
    }

    @Override
    public boolean hasNext() {
        return nextNode != null;
    }

    @Override
    public IReadNode next() {
        skipSpaces();
        return currNode;
    }
}
