package rule_wordtrait.impl;

import langdefsub.PAR_TYPE;
import langdefsubalgo.util.NumUtil;
import langdefsubalgo.util.RangeUtil;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;
import rule_wordtrait.iface.IWordTraitAction;
import rule_wordtrait.iface.IWordTraitClient;

import java.util.List;

import static langdefsub.PAR_TYPE.*;

public class WordTraitActionImplGroup {

    /*=====Parentheses/Curly Brace separation=========================================================================*/

    public static abstract class ActionSplitSurrounded implements IWordTraitAction{
        protected final ITokenizer parTokenizer;

        public ActionSplitSurrounded(char oSymbol, char cSymbol) {
            parTokenizer = Tokenizer.builder().delimiters(oSymbol, cSymbol).skipSymbols('\'').
                    keepSkipSymbol().keepEscapeSymbol().build();
        }
    }

    public static class ActionOnRxWordRangeYes extends ActionSplitSurrounded{
        private static ActionOnRxWordRangeYes instance;

        public static ActionOnRxWordRangeYes initInstance(char oSymbol, char cSymbol){
            return (instance == null)? (instance = new ActionOnRxWordRangeYes(oSymbol, cSymbol)): instance;
        }

        private ActionOnRxWordRangeYes(char oSymbol, char cSymbol) {
            super(oSymbol, cSymbol);
        }

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            client.receiveContent(parType);

            // get text part
            String[] funParts = parTokenizer.setText(text).parse().toArray();
            if(funParts.length != 2){
                return false;
            }
            client.receiveContent(funParts[0]);

            // get range part
            List<String> stringList = RangeUtil.initInstance().rangeToList(funParts[1]);
            return stringList.size() == 2 && NumUtil.initInstance().numbersToClient(client, stringList);
        }
    }

    // separate FUNCTION(PAR) to FUNCTION PAR
    public static class ActionOnFunParYes extends ActionSplitSurrounded {
        private static ActionOnFunParYes instance;

        private ActionOnFunParYes(char oSymbol, char cSymbol) {
            super(oSymbol, cSymbol);
        }

        public static ActionOnFunParYes initInstance(char oSymbol, char cSymbol){
            return (instance == null)? (instance = new ActionOnFunParYes(oSymbol, cSymbol)): instance;
        }

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            client.receiveContent(parType);
            String[] funParts = parTokenizer.setText(text).parse().toArray();
            if(funParts.length == 2){
                client.receiveContent(funParts);
                return true;
            }
            if(funParts.length == 1){// empty par
                client.receiveContent(new String[]{funParts[0], ""});
                return true;
            }
            return false;
        }
    }

    public static class ActionOnNo implements IWordTraitAction {
        private static ActionOnNo instance;

        public static ActionOnNo initInstance(){
            return (instance == null)? (instance = new ActionOnNo()): instance;
        }

        private ActionOnNo() {}

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            client.receiveContent(parType);
            client.receiveContent(text);
            return true;
        }
    }

    /*=====Array extractors===========================================================================================*/

    public static abstract class ActionList implements IWordTraitAction {
        protected final ITokenizer tokenizer;
        protected final char sep;

        public ActionList(char sep) {
            this.sep = sep;
            tokenizer = Tokenizer.builder().delimiters(sep).skipSymbols('\'').keepSkipSymbol().
                    keepEscapeSymbol().build();
        }
    }

    public static class ActionStringIds extends ActionList {
        public static ActionStringIds initInstance(char sep){
            return new ActionStringIds(sep);
        }

        private ActionStringIds(char sep) {
            super(sep);
        }

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            client.receiveContent(parType);
            client.receiveContent(tokenizer.setText(text).parse().toArray());
            return true;
        }
    }

    public static class ActionNumbers extends ActionList {
        public static ActionNumbers initInstance(char sep){
            return new ActionNumbers(sep);
        }

        private ActionNumbers(char sep) {
            super(sep);
        }

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            client.receiveContent(parType);
            List<String> stringList = tokenizer.setText(text).parse().toList();
            return NumUtil.initInstance().numbersToClient(client, stringList);
        }
    }

    public static class ActionNumRange implements IWordTraitAction {
        public static ActionNumRange initInstance(){
            return new ActionNumRange();
        }

        private ActionNumRange() {}

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            client.receiveContent(parType);
            return NumUtil.initInstance().numbersToClient(
                    client,
                    RangeUtil.initInstance().rangeToList(text)
            );
        }
    }

    /*=====Singular extractors========================================================================================*/

    public static class ActionStringId implements IWordTraitAction {
        private static ActionStringId instance;

        public static ActionStringId initInstance(){
            return (instance == null)? (instance = new ActionStringId()): instance;
        }

        private ActionStringId(){}

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            if("true".equalsIgnoreCase(text)){
                client.receiveContent(BOOL_PAR);
                client.receiveContent(1);
            }
            else if ("false".equalsIgnoreCase(text)){
                client.receiveContent(BOOL_PAR);
                client.receiveContent(0);
            }
            else{
                client.receiveContent(parType);
                client.receiveContent(text);
            }
            return true;
        }
    }

    public static class ActionNumber implements IWordTraitAction {
        private static ActionNumber instance;

        public static ActionNumber initInstance(){
            return (instance == null)? (instance = new ActionNumber()): instance;
        }

        private ActionNumber(){}

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            client.receiveContent(parType);
            client.receiveContent(Integer.parseInt(text));
            return true;
        }
    }

    /*=====Quoted extractors==========================================================================================*/

    public static class ActionQuotedString implements IWordTraitAction {
        private static ActionQuotedString instance;

        public static ActionQuotedString initInstance(){
            return (instance == null)? (instance = new ActionQuotedString()): instance;
        }

        private ActionQuotedString() {}

        protected String unquote(String text){
            int len = text.length();
            if(len >= 2 && text.charAt(0) == '\'' && text.charAt(len - 1) == '\'' ){
                return text.substring(1, len - 1);
            }
            return null;
        }

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            String unquote = this.unquote(text);
            if(unquote == null){
                return false;
            }
            client.receiveContent(parType);
            client.receiveContent(unquote);
            return true;
        }
    }

    public static class ActionQuotedStringList extends ActionQuotedString {
        private static ActionQuotedStringList instance;

        public static ActionQuotedStringList initInstance(){
            return (instance == null)? (instance = new ActionQuotedStringList()): instance;
        }

        private final ITokenizer listTokenizer;

        private ActionQuotedStringList() {
            listTokenizer = Tokenizer.builder().delimiters(',').skipSymbols('\'').keepSkipSymbol().
                    keepEscapeSymbol().build();
        }

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            List<String> quotedList = listTokenizer.setText(text).parse().toList();
            String[] unquotedList = new String[quotedList.size()];
            for(int i = 0; i < quotedList.size(); i++){
                unquotedList[i] = this.unquote(quotedList.get(i));
                if(unquotedList[i] == null){
                    return false;
                }
            }
            client.receiveContent(parType);
            client.receiveContent(unquotedList);
            return true;
        }
    }

    public static class ActionEmptyPar implements IWordTraitAction{
        private static ActionEmptyPar instance;

        public static ActionEmptyPar initInstance(){
            return (instance == null)? (instance = new ActionEmptyPar()): instance;
        }

        private ActionEmptyPar() {}

        @Override
        public boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text) {
            client.receiveContent(parType);
            client.receiveContent("");
            return true;
        }
    }

    /*=====Utilities==================================================================================================*/

}
