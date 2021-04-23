package rule_wordtrait.impl;

import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;
import rule_wordtrait.iface.IWordTraitAction;
import rule_wordtrait.iface.IWordTraitClient;

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
        public void doAction(IWordTraitClient client, String text) {
            client.receiveContent(tokenizer.setText(text).parse().toArray());
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
        public void doAction(IWordTraitClient client, String text) {
            List<String> stringList = tokenizer.setText(text).parse().toList();
            int[] intList = new int[stringList.size()];
            for(int i = 0; i < stringList.size(); i++){
                intList[i] = Integer.parseInt(stringList.get(i)); // validated by WORD_TRAIT pattern match
            }
            client.receiveContent(intList);
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
        public void doAction(IWordTraitClient client, String text) {
            client.receiveContent(text);
        }
    }

    public static class WordTraitActionNumber implements IWordTraitAction {
        private static WordTraitActionNumber instance;

        public static WordTraitActionNumber initInstance(){
            return (instance == null)? (instance = new WordTraitActionNumber()): instance;
        }

        private WordTraitActionNumber(){}

        @Override
        public void doAction(IWordTraitClient client, String text) {
            client.receiveContent(Integer.parseInt(text));
        }
    }

    /*=====Quoted extractors==========================================================================================*/

    public static class WordTraitActionQuoted implements IWordTraitAction {
        private static WordTraitActionQuoted instance;

        public static WordTraitActionQuoted initInstance(){
            return (instance == null)? (instance = new WordTraitActionQuoted()): instance;
        }

        private WordTraitActionQuoted() {}

        @Override
        public void doAction(IWordTraitClient client, String text) {
            client.receiveContent(text.substring(1, text.length() - 1));
        }
    }

    public static class WordTraitActionQuotedList implements IWordTraitAction {
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
        public void doAction(IWordTraitClient client, String text) {
            List<String> quotedList = listTokenizer.setText(text).parse().toList();
            String[] unquotedList = new String[quotedList.size()];
            for(int i = 0; i < quotedList.size(); i++){
                unquotedList[i] = quotedList.get(i).substring(1, quotedList.get(i).length() - 1);
            }
            client.receiveContent(unquotedList);
        }
    }
}
