package wordtraitutil.impl;

import err.ERR_TYPE;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;
import wordtraitutil.iface.IWordTraitAction;
import wordtraitutil.iface.IWordTraitClient;

import java.util.List;

public class WordTraitActionImplGroup {

    /*=====Array extractors===========================================================================================*/

    public static abstract class WordTraitActionList implements IWordTraitAction {
        protected final ITokenizer tokenizer;
        protected final char sep;

        public WordTraitActionList(char sep) {
            this.sep = sep;
            tokenizer = Tokenizer.builder().delimiters(sep).skipSymbols('\'').keepSkipSymbol().
                    keepEscapeSymbol().build();
        }
    }

    public static class WordTraitActionStrings extends WordTraitActionList {
        public static WordTraitActionStrings initInstance(char sep){
            return new WordTraitActionStrings(sep);
        }

        private WordTraitActionStrings(char sep) {
            super(sep);
        }

        @Override
        public ERR_TYPE doAction(IWordTraitClient client, String text) {
            client.receiveContent(tokenizer.setText(text).parse().toArray());
            return ERR_TYPE.NONE;
        }
    }

    public static class WordTraitActionNumbers extends WordTraitActionList {
        public static WordTraitActionNumbers initInstance(char sep){
            return new WordTraitActionNumbers(sep);
        }

        private WordTraitActionNumbers(char sep) {
            super(sep);
        }

        @Override
        public ERR_TYPE doAction(IWordTraitClient client, String text) {
            List<String> stringList = tokenizer.setText(text).parse().toList();
            int[] intList = new int[stringList.size()];
            for(int i = 0; i < stringList.size(); i++){
                intList[i] = Integer.parseInt(stringList.get(i)); // validated by WORD_TRAIT pattern match
            }
            client.receiveContent(intList);
            return ERR_TYPE.NONE;
        }
    }

    /*=====Singular extractors========================================================================================*/

    public static class WordTraitActionString implements IWordTraitAction {
        private static WordTraitActionString instance;

        public static WordTraitActionString initInstance(){
            return (instance == null)? (instance = new WordTraitActionString()): instance;
        }

        private WordTraitActionString(){}

        @Override
        public ERR_TYPE doAction(IWordTraitClient client, String text) {
            client.receiveContent(text);
            return ERR_TYPE.NONE;
        }
    }

    public static class WordTraitActionNumber implements IWordTraitAction {
        private static WordTraitActionNumber instance;

        public static WordTraitActionNumber initInstance(){
            return (instance == null)? (instance = new WordTraitActionNumber()): instance;
        }

        private WordTraitActionNumber(){}

        @Override
        public ERR_TYPE doAction(IWordTraitClient client, String text) {
            client.receiveContent(Integer.parseInt(text));
            return ERR_TYPE.NONE;
        }
    }

    /*=====Quoted extractors==========================================================================================*/

    public static abstract class WordTraitActionQuotedBase implements IWordTraitAction {
        protected final ITokenizer quoteTokenizer;
        protected final String quote;

        public WordTraitActionQuotedBase() {
            this.quote = "'";
            quoteTokenizer = Tokenizer.builder().delimiters('\'').tokenizeDelimiter().
                    keepEscapeSymbol().build();
        }
        protected String unquote(String text){
            List<String> tok = quoteTokenizer.setText(text).parse().toList();
            if(tok.size() == 3 && quote.equals(tok.get(0)) &&  quote.equals(tok.get(2))){
                return tok.get(1);
            }
            return null;
        }
    }

    public static class WordTraitActionQuoted extends WordTraitActionQuotedBase {
        private static WordTraitActionQuoted instance;

        public static WordTraitActionQuoted initInstance(){
            return (instance == null)? (instance = new WordTraitActionQuoted()): instance;
        }

        private WordTraitActionQuoted() {}

        @Override
        public ERR_TYPE doAction(IWordTraitClient client, String text) {
            String unquote = this.unquote(text);
            if(unquote != null){
                client.receiveContent(unquote);
                return ERR_TYPE.NONE;
            }
            return ERR_TYPE.UNKNOWN_PATTERN;
        }
    }

    public static class WordTraitActionQuotedList extends WordTraitActionQuotedBase {
        private static WordTraitActionQuotedList instance;

        public static WordTraitActionQuotedList initInstance(){
            return (instance == null)? (instance = new WordTraitActionQuotedList()): instance;
        }

        private final ITokenizer listTokenizer;

        private WordTraitActionQuotedList() {
            listTokenizer = Tokenizer.builder().delimiters(',').skipSymbols('\'').keepSkipSymbol().
                    keepEscapeSymbol().build();
        }

        @Override
        public ERR_TYPE doAction(IWordTraitClient client, String text) {
            List<String> quotedList = listTokenizer.setText(text).parse().toList();
            String[] unquotedList = new String[quotedList.size()];
            for(int i = 0; i < quotedList.size(); i++){
                unquotedList[i] = this.unquote(quotedList.get(i));
                if(unquotedList[i] == null){
                    return ERR_TYPE.UNKNOWN_PATTERN;
                }
            }
            return ERR_TYPE.NONE;
        }
    }
}
