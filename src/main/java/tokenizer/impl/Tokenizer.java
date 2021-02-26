package tokenizer.impl;

import tokenizer.iface.ITokenizer;

import java.util.ArrayList;
import java.util.Stack;

/**A more complex string tokenizer.
 * Supports multiple delimiters
 * Ignores adjacent delimiters to prevent empty elements
 * Supports 'skip area' (quoted or bracketed text). Tokenizer leaves these areas joined.
 * Supports multiple, nested skip symbols. Outermost symbol defines skip area.
 * Option to keep or discard delimiters, skip symbols.
 * Use builder to set options.
 *
 * Sample usage:
 *   String text = "Sentence__with_(too_many_'delims')_and_quotes__";
 *   String[] tok = new Tokenizer.Builder().delimiters(" _").skipSymbols("('").build().parse(text).getArray();
 * Output:
 * 	  Sentence
 * 	  with
 * 	  too_many_'delims'
 * 	  and
 * 	  quotes
 */
public class Tokenizer implements ITokenizer {
    protected  String delimiters;           // input text, list of delimiters text,
    protected char[] oMap, cMap;            // matched open/close skip char arrays
    protected ArrayList<String> tokens;     // output
    protected Stack<Character> cSymbols;    // Closing symbol
    protected boolean tokenizeDelimiter;   // keep delims, skips to separate list
    protected boolean delimiterOnce;
    protected boolean keepSkipSymbol;

    public Tokenizer(){
        delimiters = " ";
        tokenizeDelimiter = false;
        keepSkipSymbol = false;
    }

    /*====Private parts===============================================================================================*/

    private void setMap(String skips){
        // map openers to closers, using symbols from arg
        // if you want different symbols, pass arrays with Builder
        oMap =  new char[skips.length()];
        cMap =  new char[skips.length()];
        char[] openers = new char[]{'(','{','[','<','"','\''};
        char[] closers = new char[]{')','}',']','>','"','\''};
        int to = 0;
        for (int i = 0; i < openers.length; i++) {
            if(skips.indexOf(openers[i])!=-1){
                oMap[to]=openers[i];
                cMap[to]=closers[i];
                to++;
            }
        }
        //Commons.disp(oMap, "oMap");
        //Commons.disp(cMap, "cMap");
    }

    private boolean isDelimiter(char symb){
        return delimiters.indexOf(symb) != -1;
    }

    private boolean haveText(int i, int j){
        return i != j;
    }

    private boolean enterSkipArea(char symbol){
        for(int i=0; i<oMap.length; i++){
            if(symbol == oMap[i]){
                this.cSymbols.push(cMap[i]);// important side effect
                return true;
            }
        }
        return false;
    }

    private boolean inSkipArea(){
        return !cSymbols.isEmpty();
    }

    private boolean leaveSkipArea(char symbol){
        if(cSymbols.peek().equals(symbol)){
            cSymbols.pop();
            return true;
        }
        return false;
    }

    private boolean noMoreSkips(){
        return cSymbols.empty();
    }

    /*====Public parts================================================================================================*/

    @Override
    public ITokenizer parse(String text) {
        cSymbols = new Stack<>();
        this.tokens = new ArrayList<>();

        int i = 0, j = 0;
        for (i = 0; i < text.length(); i++) {
            char curr = text.charAt(i);

            if(inSkipArea()){
                if(leaveSkipArea(curr)){
                    if(noMoreSkips() && haveText(i, j)){
                        int offset = (keepSkipSymbol)? 1 : 0;
                        tokens.add(text.substring(j, i + offset));
                        j = i + 1;
                    }

                }
                else if(enterSkipArea(curr)){}
            }
            else if(enterSkipArea(curr)){
                if(haveText(i, j)){
                    tokens.add(text.substring(j, i));
                    j = i;
                }
                if(!keepSkipSymbol){
                    j += 1;
                }
            }
            else if(isDelimiter(curr)){
                if(haveText(i, j)){
                    tokens.add(text.substring(j, i));
                }
                if(tokenizeDelimiter){
                    if(!delimiterOnce || i != j){
                        tokens.add(text.substring(i, i + 1));
                    }
                }
                j = i + 1;
            }
        }

        if(haveText(i, j)){
            tokens.add(text.substring(j, i));
        }
        return this;
    }

    @Override
    public ArrayList<String> getArrayList() {
        return this.tokens;
    }

    @Override
    public String[] getArray() {
        return this.tokens.toArray(new String[0]);
    }

    public static class Builder implements ITokenizer.Builder {
        Tokenizer built;

        public Builder(){
            built = new Tokenizer();
        }

        @Override
        public ITokenizer.Builder delimiters(String delimiters) {
            built.delimiters = delimiters;
            return this;
        }

        @Override
        public ITokenizer.Builder delimiters(char oneDelimiter) {
            built.delimiters = String.valueOf(oneDelimiter);
            return this;
        }

        @Override
        public ITokenizer.Builder skipSymbols(String openingSymbols) {
            built.setMap(openingSymbols);
            return this;
        }

        @Override
        public ITokenizer.Builder skipSymbols(char oneOpeningSymbol) {
            built.setMap(String.valueOf(oneOpeningSymbol));
            return this;
        }

        @Override
        public ITokenizer.Builder skipSymbols(char[] oMap, char[] cMap) {
            built.oMap = oMap;
            built.cMap = cMap;
            return this;
        }

        @Override
        public ITokenizer.Builder keepSkipSymbol() {
            built.keepSkipSymbol = true;
            return this;
        }

        @Override
        public ITokenizer.Builder tokenizeDelimiter() {
            built.tokenizeDelimiter = true;
            return this;
        }

        @Override
        public ITokenizer.Builder tokenizeDelimiterOnce() {
            built.tokenizeDelimiter = true;
            built.delimiterOnce = true;
            return this;
        }


        @Override
        public ITokenizer build() {
            if(built.oMap == null){
                built.oMap = new char[0];
            }
            return built;
        }
    }
}
