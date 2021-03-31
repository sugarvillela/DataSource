package tokenizer.toknode;

import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import tokenizer.iface.ITokenizer;

import java.util.ArrayList;
import java.util.Arrays;

public class NodeTokenizer {
    private final ITokenizer tokenizer;
    IReadNode[] nodes;

    public NodeTokenizer() {
        this(Glob.TOKENIZER);
    }
    public NodeTokenizer(ITokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public NodeTokenizer parse(String text, String containerSource, int containerRow, boolean containerHasNext) {
        text = text.replace("\t", "    ");

        ArrayList<String> tokens = tokenizer.setText(text).parse().toList();
        int[] indents = tokenizer.indents();
        nodes = new IReadNode[tokens.size()];
        int k = 0;
        for(int i = 0; i < tokens.size(); i++){
            nodes[k++] = new ReadNode(
                    containerSource, containerRow, i, tokens.get(i), text.trim(), (i == tokens.size() - 1), containerHasNext, indents[i]
            );
        }
        return this;
    }

    public NodeTokenizer parse(String text){
        return this.parse(text, "", 0, true);
    }

    public IReadNode[] getArray() {
        return nodes;
    }
}
