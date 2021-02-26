package datasource.core;

import tokenizer.toknode.NodeTokenizer;

/** source for tokenizing a row and iterating it as a column */
public class SourceCol extends SourceNode{
    public SourceCol(String text, String containerSource, int containerRow, boolean containerHasNext){
        super(
            new NodeTokenizer().parse(text, containerSource, containerRow, containerHasNext).getArray()
        );
    }
}
