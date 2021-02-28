package readnode.impl;

import langdef.CMD;
import readnode.iface.IReadNode;
import textevent.iface.ITextEventNode;

public abstract class ReadNodeBase implements IReadNode {
    protected static final String FORMAT_STATUS = "%s,%d,%d";
    protected static final String FORMAT_FRIENDLY_STATUS = "%s line %d word %d";
    protected static int uqValue = 0;

    protected final int sortValue;
    protected final int row, col;
    protected final String source;

    protected String text;
    protected boolean active;
    protected ITextEventNode textEvent;

    public ReadNodeBase(String source, int row, int col, String text){
        this.sortValue = uqValue++;

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
    public void setLangStruct(ITextEventNode textEventNode) {
        this.textEvent = textEventNode;
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
    public ITextEventNode textEvent() {
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
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void setHasNext(boolean hasNext) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public int indent() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public boolean endLine() {
        throw new IllegalStateException("Not implemented");
    }
    @Override
    public boolean hasNext() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public String indentedText() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public String friendlyString() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public String csvString() {
        throw new IllegalStateException("Not implemented");
    }

    /*================================================================================================================*/

    @Override
    public CMD cmd() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public String key() {
        throw new IllegalStateException("Not implemented");
    }
}
