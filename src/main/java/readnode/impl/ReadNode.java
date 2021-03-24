package readnode.impl;

import readnode.iface.IReadNode;
import textevent.iface.ITextEventNode;

import java.util.ArrayList;

import static runstate.Glob.NULL_TEXT;

public class ReadNode extends ReadNodeBase {
    private static final String FORMAT_CSV = "%s,%d,%d,%d,%d,%d,%d,%s,%s";

    private final String containerText;
    private final int indent;
    private boolean endLine, hasNext;

    // row implementations use this constructor; unused immutables kill to default
    public ReadNode(String source, int row, String text, boolean hasNext){
        this(source, row, 0, text, null, true, hasNext, 0);
    }

    // col implementations use this constructor; all immutables exposed
    public ReadNode(String source, int row, int col, String text, String containerText, boolean endLine, boolean hasNext, int indent) {
        super(source, row, col, text);

        this.containerText = containerText;

        this.endLine = endLine;
        this.indent = indent;

        this.active = true;
        this.hasNext = hasNext;
    }

    @Override
    public void setEndLine(boolean endLine) {
        this.endLine = endLine;
    }

    // state setters
    @Override
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public String containerText() {
        return containerText;
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
        //out.put("sortValue " + sortValue);
        if(source != null){ out.add(source); }
        out.add("row " + row);
        out.add("col " + col);
        out.add("indent " + indent);
        if(!active){ out.add("inactive"); }
        if(endLine){ out.add("endLine"); }
        if(hasNext){ out.add("hasNext"); }
        if(text != null){ out.add("text: " + text); }
        if(textEvent != null){ out.add("textEvent: " + textEvent.friendlyString()); }
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
                ((textEvent == null)? NULL_TEXT : textEvent.csvString())
        );
    }


    /*=====Builder====================================================================================================*/

    public static NodeBuilder builder(){
        return new NodeBuilder();
    }

    public static class NodeBuilder{
        private String source, text, containerText;
        private int row, col, indent;
        private boolean endLine, hasNext;
        ITextEventNode textEvent;

        public NodeBuilder(){
            endLine = true;
        }

        public NodeBuilder copy(IReadNode readNode){
            this.source = readNode.source();
            this.text = readNode.text();
            this.containerText = readNode.containerText();
            this.row = readNode.row();
            this.col = readNode.col();
            this.indent = readNode.indent();
            this.endLine = readNode.endLine();
            this.hasNext = readNode.hasNext();
            this.textEvent = readNode.textEvent();
            return this;
        }
        public NodeBuilder source(String source){
            this.source = source;
            return this;
        }
        public NodeBuilder text(String text){
            this.text = text;
            return this;
        }
        public NodeBuilder containerText(String containerText){
            this.containerText = containerText;
            return this;
        }
        public NodeBuilder row(int row){
            this.row = row;
            return this;
        }
        public NodeBuilder col(int col){
            this.col = col;
            return this;
        }
        public NodeBuilder indent(int indent){
            this.indent = indent;
            return this;
        }
        public NodeBuilder endLine(boolean endLine){
            this.endLine = endLine;
            return this;
        }
        public NodeBuilder hasNext(boolean hasNext){
            this.hasNext = hasNext;
            return this;
        }
        public NodeBuilder textEvent(ITextEventNode textEvent){
            this.textEvent = textEvent;
            return this;
        }
        public IReadNode build(){
            IReadNode built = new ReadNode(source, row, col, text, containerText, endLine, hasNext, indent);
            built.setTextEvent(textEvent);
            return built;
        }
    }


}
