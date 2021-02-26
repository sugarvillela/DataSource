package readnode.impl;

import readnode.iface.IReadNode;
import textpattern.TEXT_PATTERN;

import java.util.ArrayList;

public class ReadNode implements IReadNode {
    private static final String FORMAT_STATUS = "%s,%d,%d";
    private static final String FORMAT_FRIENDLY_STATUS = "%s line %d word %d";
    private static final String FORMAT_CSV = "%s,%d,%d,%d,%d,%d,%d,%s,%s";

    private static int uqValue = 0;

    private final int sortValue;
    private final int row, col;
    private final String source, text;
    private final boolean endLine;
    private final int indent;

    private boolean active, hasNext;
    private TEXT_PATTERN textPattern;

    // immutable setters

    // row implementations use this constructor; unused immutables set to default
    public ReadNode(String source, int row, String text, boolean hasNext){
        this(source, row, 0, text, true, hasNext, 0);
    }

    // col implementations use this constructor; all immutables exposed
    public ReadNode(String source, int row, int col, String text, boolean endLine, boolean hasNext, int indent) {
        this.sortValue = uqValue++;

        this.source = source;
        this.row = row;
        this.col = col;
        this.text = text;
        this.endLine = endLine;
        this.indent = indent;

        this.active = true;
        this.hasNext = hasNext;
    }

    // state setters

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public void setTextPattern(TEXT_PATTERN textPattern) {
        this.textPattern = textPattern;
    }


    // immutable getters

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
    public int indent() {
        return indent;
    }

    @Override
    public boolean endLine() {
        return endLine;
    }


    // state getters

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public boolean hasPattern() {
        return textPattern != null;
    }

    @Override
    public TEXT_PATTERN textPattern() {
        return textPattern;
    }


    // to string

    @Override
    public String indentedText() {
        return new String(new char[indent]).replace('\0', ' ') + text;
    }

    @Override
    public String statusString() {
        return String.format(FORMAT_STATUS, source, row, col);
    }

    @Override
    public String friendlyStatusString() {
        return String.format(FORMAT_FRIENDLY_STATUS, source, row, col);
    }

    @Override
    public String friendlyString() {
        ArrayList<String> out = new ArrayList<>();
        out.add("sortValue " + sortValue);
        if(source != null){ out.add(source); }
        out.add("row " + row);
        out.add("col " + col);
        out.add("indent " + indent);
        if(!active){ out.add("inactive"); }
        if(endLine){ out.add("endLine"); }
        if(hasNext){ out.add("hasNext"); }
        if(text != null){ out.add("text: " + text); }
        if(textPattern != null){ out.add("textPattern: " + textPattern.toString()); }
        return String.join(", ", out);
    }

    @Override
    public String csvString() {
        return String.format(FORMAT_CSV,
                source, row, col, indent,
                (active? 1 : 0),
                (endLine? 1 : 0),
                (hasNext? 1 : 0),
                text,
                ((textPattern == null)? "-" : textPattern.toString())
        );
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
}
