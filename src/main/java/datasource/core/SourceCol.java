package datasource.core;

import runstate.Glob;
import tokenizer.toknode.NodeTokenizer;

/** source for tokenizing a row and iterating it as a column */
public class SourceCol extends SourceNode{
    public SourceCol(String text, String containerSource, int containerRow, boolean containerHasNext){
        super(
            new NodeTokenizer(Glob.TOKENIZER).parse(text, containerSource, containerRow, containerHasNext).getArray()
        );
    }
}
