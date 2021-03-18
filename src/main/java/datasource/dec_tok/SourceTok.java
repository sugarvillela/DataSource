package datasource.dec_tok;

import datasource.core.SourceCol;
import datasource.iface.IDataSource;
import readnode.iface.IReadNode;
import runstate.Glob;
import tokenizer.impl.SimpleTok;

public class SourceTok implements IDataSource {
    private static final SimpleTok tokenizer = new SimpleTok();
    private final IDataSource rowSource;
    private IDataSource colSource;
    private IReadNode currNode;
    private boolean more;

    public SourceTok(IDataSource rowSource) {
        this.rowSource = rowSource;
        if(this.hasData()){
            more = true;
            refreshColSource();
        }
    }

    @Override
    public String sourceName() {
        return rowSource.sourceName();
    }

    @Override
    public boolean hasData() {
        return rowSource != null && rowSource.hasData();
    }

    @Override
    public boolean hasNext() {
        return more;
    }

    @Override
    public IReadNode next() {
        if(!colSource.hasNext()){  // end of row
            refreshColSource();
        }
        currNode = colSource.next();
        if(currNode == null || (!currNode.hasNext() && currNode.endLine())){
            more = false;
        }
        return currNode;
    }

    private void refreshColSource(){
        IReadNode rowNode = rowSource.next();
        if(rowNode != null){
            colSource = new SourceCol(rowNode.text(), rowNode.source(), rowNode.row(), rowNode.hasNext());
        }
    }

    @Override
    public boolean hasPeekBack() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return false;
    }
    @Override
    public boolean hasPeekAhead() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return false;
    }
    @Override
    public IReadNode peekBack() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return null;
    }
    @Override
    public IReadNode peekAhead() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return null;
    }

}
