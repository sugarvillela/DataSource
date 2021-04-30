package langdefsubalgo.util;

import langdef.LangConstants;
import rule_wordtrait.iface.IWordTraitClient;
import rule_wordtrait.impl.WordTraitActionImplGroup;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class RangeUtil {
    private static RangeUtil instance;

    public static RangeUtil initInstance() {
        return (instance == null) ? (instance = new RangeUtil()) : instance;
    }

    protected final ITokenizer tokenizer;

    public RangeUtil() {
        tokenizer = Tokenizer.builder().delimiters(LangConstants.RANGE_SEP).skipSymbols('\'').keepSkipSymbol().
                keepEscapeSymbol().build();
    }

    /*=====Translate regex symbols to range notation==================================================================*/

    public boolean translateSymbolToRange(IWordTraitClient client, String text){
        int len = text.length();
        char end = text.charAt(len - 1);
        switch(end){
            case '*':
                client.receiveContent(0, 1024);
                return true;
            case '+':
                client.receiveContent(1, 1024);
                return true;
            case '?':
                client.receiveContent(0, 1);
                return true;
            default:
                return false;
        }
    }

    /*=====Split at ':' correcting for range above and range below====================================================*/

    private boolean startsWithSep(String text) {
        return text.charAt(0) == LangConstants.RANGE_SEP;
    }

    private boolean endsWithSep(String text) {
        return text.charAt(text.length() - 1) == LangConstants.RANGE_SEP;
    }

    public List<String> rangeToList(String text) {
        List<String> stringList;
        if (startsWithSep(text)) {
            stringList = new ArrayList(2);
            stringList.add("0");
            stringList.add(text.substring(1));
        } else if (endsWithSep(text)) {
            stringList = new ArrayList(2);
            stringList.add(text.substring(0, text.length() - 1));
            stringList.add("1024");
        } else {
            stringList = tokenizer.setText(text).parse().toList();
        }
        return stringList;
    }
}
