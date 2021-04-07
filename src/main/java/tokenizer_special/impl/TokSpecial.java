package tokenizer_special.impl;

import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;
import tokenizer_special.iface.IProtectPatterns;
import tokenizer_special.iface.ITokSpecial;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static langdef.LangConstants.*;

public class TokSpecial implements ITokSpecial {
    private static TokSpecial instance;

    public static TokSpecial initInstance(){
        return (instance == null)? (instance = new TokSpecial()): instance;
    }

    private final IProtectPatterns protectPatterns;
    private final ITokenizer tokenizer;
    private ArrayList<String> result;

    private TokSpecial(){
        protectPatterns = new ProtectPatterns();
        tokenizer = Tokenizer.builder().
            delimiters(ITEM_OPEN.charAt(0), ITEM_CLOSE.charAt(0),COND_OPEN.charAt(0), COND_CLOSE.charAt(0)).
                skipSymbols(' ',' ').tokenizeDelimiter().build();
    }



    @Override
    public void initPatterns(Pattern... patterns) {
        protectPatterns.initPatterns(patterns);
    }

    @Override
    public boolean tryTok(String text) {
        if(protectPatterns.tryProtect(text)){
            text = protectPatterns.getResult();
        }
        return (result = tokenizer.setText(text).parse().toList()).size() > 1;
    }

    @Override
    public ArrayList<String> getResult() {
        return result;
    }
}
