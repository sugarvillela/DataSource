package readnode.impl;

import langdef.CMD;
import langdef.iface.LANG_STRUCT;
import readnode.iface.IReadNode;

public abstract class ReadNodeBase implements IReadNode {
    protected static final String FORMAT_STATUS = "%s,%d,%d";
    protected static final String FORMAT_FRIENDLY_STATUS = "%s line %d word %d";
    protected static int uqValue = 0;

    protected final int sortValue;
    protected final int row, col;
    protected final String source, text;

    protected boolean active;
    protected LANG_STRUCT langStruct;

    public ReadNodeBase(String source, int row, int col, String text){
        this.sortValue = uqValue++;

        this.source = source;
        this.row = row;
        this.col = col;
        this.text = text;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setLangStruct(LANG_STRUCT langStruct) {
        this.langStruct = langStruct;
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
    public boolean hasPattern() {
        return langStruct != null;
    }

    @Override
    public LANG_STRUCT langStruct() {
        return langStruct;
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
