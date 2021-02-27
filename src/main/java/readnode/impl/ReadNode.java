package readnode.impl;

import java.util.ArrayList;

public class ReadNode extends ReadNodeBase {
    private static final String FORMAT_CSV = "%s,%d,%d,%d,%d,%d,%d,%s,%s";

    private final boolean endLine;
    private final int indent;

    private boolean hasNext;

    // row implementations use this constructor; unused immutables set to default
    public ReadNode(String source, int row, String text, boolean hasNext){
        this(source, row, 0, text, true, hasNext, 0);
    }

    // col implementations use this constructor; all immutables exposed
    public ReadNode(String source, int row, int col, String text, boolean endLine, boolean hasNext, int indent) {
        super(source, row, col, text);

        this.endLine = endLine;
        this.indent = indent;

        this.active = true;
        this.hasNext = hasNext;
    }

    // state setters
    @Override
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    // immutable getters
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
    public boolean hasNext() {
        return hasNext;
    }

    // to string

    @Override
    public String indentedText() {
        return new String(new char[indent]).replace('\0', ' ') + text;
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
        if(langStruct != null){ out.add("langStruct: " + langStruct.toString()); }
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
                ((langStruct == null)? "-" : langStruct.toString())
        );
    }



}
