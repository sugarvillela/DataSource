package tokenizer.toknode;

import readnode.iface.IReadNode;
import readnode.impl.ReadNode;

public class NodeTokenizer {
    String[] tokens;
    int[] indents;
    IReadNode[] nodes;

    public NodeTokenizer() {}

    private boolean isWhiteSpace(char c){
        return ((int)c) < 33;
    }
    private void initData(String text){
        int size = 0;
        int i, j = 0;
        for(i = 0; i < text.length(); i++){
            if(this.isWhiteSpace(text.charAt(i))){
                if( i != j ){
                    size++;
                }
                j=i+1;
            }
        }
        if( i != j ){
            size++;
        }
        tokens = new String[size];
        indents = new int[size];
        nodes = new IReadNode[size];
    }

    private void fillData(String text){
        // Indents under-counted by 1, except on initial token.
        // When using String.join to rebuild, use a space as delimiter.
        // This puts goBack the missing indent, except on the initial token.
        int i, j = 0, k = 0, n = 0;
        for(i = 0; i < text.length(); i++){
            if(this.isWhiteSpace(text.charAt(i))){
                if( i != j ){
                    tokens[k] = text.substring(j, i);
                    indents[k] = j - n;
                    n = i + 1;
                    k++;
                }
                j = i + 1;
            }
        }
        if( i != j ){
            tokens[k] = text.substring(j);
            indents[k] = j - n;
        }
    }

    public NodeTokenizer parse(String text, String containerSource, int containerRow, boolean containerHasNext) {
        initData(text); // Rehearse to get size
        fillData(text); // tokenize, count indents indents under-counted by 1, except initial

        int k = 0;
        for(int i = 0; i < tokens.length; i++){
            nodes[k++] = new ReadNode(
                    containerSource, containerRow, i, tokens[i], text.trim(), (i == tokens.length - 1), containerHasNext, indents[i]
            );
        }

        tokens = null;
        indents = null;
        return this;
    }

    public NodeTokenizer parse(String text){
        return this.parse(text, "", 0, true);
    }

    public IReadNode[] getArray() {
        return nodes;
    }
}
