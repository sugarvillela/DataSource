package tokenizer.impl;

import tokenizer.iface.ITokenizer;

import java.util.ArrayList;

/**A simple string tokenizer.
 * Supports single delimiter
 * Ignores adjacent delimiters to prevent empty elements
 * Option to limit number of splits */
public class SimpleTok implements ITokenizer {
    private final char delimiter;
    private final int limit;
    String[] tokens;

    public SimpleTok(){
        this( ' ', 0x7FFFFFFF );
    }
    public SimpleTok(char delimiter ){
        this( delimiter, 0x7FFFFFFF );
    }
    public SimpleTok(char delimiter, int limit ){
        this.delimiter = delimiter;
        this.limit = limit;
    }

    @Override
    public ITokenizer parse(String text) {
        // Rehearse to get size
        int count = 0;
        int i, j = 0, k = 0;
        for(i = 0; i < text.length(); i++){
            if( text.charAt(i) == delimiter){
                if( i != j ){
                    count++;
                    // Limit size, if limit passed
                    if( count == limit ){
                        i = j;
                        break;
                    }
                }
                j=i+1;
            }
        }
        if( i != j ){
            count++;
        }
        // Set array and run again to populate
        tokens = new String[count];
        j = 0;
        for(i = 0; i < text.length(); i++){
            if( text.charAt(i) == delimiter){
                if( i != j ){
                    if( k >= limit-1){
                        break;
                    }
                    tokens[k] = text.substring(j, i);
                    k++;

                }
                j=i+1;
            }
        }
        if( i != j ){
            tokens[k] = text.substring(j);
        }
        return this;
    }

    @Override
    public ArrayList<String> getArrayList() {
        ArrayList<String> out = new ArrayList<>(tokens.length);
        for(String tok: tokens) {
            out.add(tok);
        }
        return out;
    }

    @Override
    public String[] getArray() {
        return tokens;
    }
}
