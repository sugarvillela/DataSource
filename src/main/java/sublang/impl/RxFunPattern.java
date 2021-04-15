package sublang.impl;

import err.ERR_TYPE;
import langdefalgo.iface.LANG_STRUCT;
import runstate.Glob;
import sublang.COMPARE;
import sublang.iface.IRxFun;
import sublang.iface.IRxFunPattern;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;

public class RxFunPattern implements IRxFunPattern {
    private static final ITokenizer T = Tokenizer.builder().delimiters(COMPARE.allChars()).
            skipSymbols('\'').tokenizeDelimiter().keepSkipSymbol().build();

    private final COMPARE compare;
    private final String textLeft, textRight;

    public RxFunPattern(String text) {
        ArrayList<String> tok = T.setText(text).parse().toList();
        if(tok.size() != 3){
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
        }

        textLeft = tok.get(0);
        compare = COMPARE.fromChar(tok.get(1).charAt(0));
        textRight = tok.get(2);

        System.out.println("===RxFunPattern===");
        System.out.println(textLeft);
        System.out.println(compare);
        System.out.println(textRight);
        System.out.println("======");
    }



    @Override
    public IRxFun[] left() {
        return new IRxFun[0];
    }

    @Override
    public COMPARE compare() {
        return null;
    }

    @Override
    public IRxFun[] right() {
        return new IRxFun[0];
    }
}
