package readnode.impl;

import langdef.CMD;
import readnode.iface.IReadNode;
import runstate.Glob;
import textevent.iface.ITextEvent;

public abstract class ReadNodeBase implements IReadNode {
    protected static final String FORMAT_STATUS = "%s,%d,%d";
    protected static final String FORMAT_FRIENDLY_STATUS = "%s line %d word %d";
    protected static int uqValue = 0;

    protected final int row, col;
    protected final String source;

    protected String text;
    protected int sortValue;
    protected boolean active;
    protected ITextEvent textEvent;

    public ReadNodeBase(String source, int row, int col, String text){
        //this.sortValue = uqValue++;

        this.source = source;
        this.row = row;
        this.col = col;
        this.text = text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setTextEvent(ITextEvent textEventNode) {
        this.textEvent = textEventNode;
    }

    @Override
    public void renumberSortValue() {
        this.sortValue = uqValue++;
    }


    // getters

    @Override
    public String source() {
        return source;
    }

    @Override
    public int row() {
        return row;
    }

    @Override
    public int col() {
        return col;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public boolean hasTextEvent() {
        return textEvent != null;
    }

    @Override
    public ITextEvent textEvent() {
        return textEvent;
    }

    // to string

    @Override
    public String statusString() {
        return String.format(FORMAT_STATUS, source, row, col);
    }

    @Override
    public String friendlyStatusString() {
        return String.format(FORMAT_FRIENDLY_STATUS, source, row, col);
    }

    // sorting

    @Override
    public int sortValue() {
        return sortValue;
    }

    @Override
    public int compareTo(IReadNode other) {
        if (other.sortValue() < this.sortValue) {
            return 1;
        }
        return (other.sortValue() > this.sortValue) ? -1 : 0;
    }

    /*================================================================================================================*/

    @Override
    public void setEndLine(boolean endLine) {
        Glob.ERR_DEV.kill("Not implemented");
    }

    @Override
    public void setHasNext(boolean hasNext) {
        Glob.ERR_DEV.kill("Not implemented");
    }

    @Override
    public int indent() {
        Glob.ERR_DEV.kill("Not implemented");
        return 0;
    }

    @Override
    public boolean endLine() {
        Glob.ERR_DEV.kill("Not implemented");
        return false;
    }
    @Override
    public boolean hasNext() {
        Glob.ERR_DEV.kill("Not implemented");
        return false;
    }

    @Override
    public String containerText() {
        Glob.ERR_DEV.kill("Not implemented");
        return null;
    }

    @Override
    public String indentedText() {
        Glob.ERR_DEV.kill("Not implemented");
        return null;
    }

    @Override
    public String friendlyString() {
        Glob.ERR_DEV.kill("Not implemented");
        return null;
    }

    @Override
    public String csvString() {
        Glob.ERR_DEV.kill("Not implemented");
        return null;
    }

    /*================================================================================================================*/

    @Override
    public CMD cmd() {
        Glob.ERR_DEV.kill("Not implemented");
        return null;
    }

    @Override
    public String key() {
        Glob.ERR_DEV.kill("Not implemented");
        return null;
    }
}
