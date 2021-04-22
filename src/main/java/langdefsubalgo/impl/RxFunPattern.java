package langdefsubalgo.impl;

import err.ERR_TYPE;
import runstate.Glob;
import langdefsub.COMPARE;
import langdefsubalgo.iface.IRxFun;
import langdefsubalgo.iface.IRxFunList;
import langdefsubalgo.iface.IRxFunPattern;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;

public class RxFunPattern implements IRxFunPattern {
    private static final ITokenizer TOK_ON_COMPARE = Tokenizer.builder().delimiters(COMPARE.allChars()).
            skipSymbols('\'').tokenizeDelimiter().keepSkipSymbol().build();

    private final COMPARE compare;
    //private final String textLeft, textRight;
    private IRxFunList listLeft, listRight;

    public RxFunPattern(String text) {
        String textLeft, textRight;
        ArrayList<String> tok = TOK_ON_COMPARE.setText(text).parse().toList();

        if(tok.size() != 3){
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
        }

        textLeft = tok.get(0);
        compare = COMPARE.fromChar(tok.get(1).charAt(0));
        textRight = tok.get(2);

        listLeft = new RxFunList(textLeft);
        listRight = new RxFunList(textRight);

        System.out.println("===RxFunPattern===");
        System.out.println(textLeft);
        System.out.println(compare);
        System.out.println(textRight);
        System.out.println("======");
    }


    @Override
    public void validate() {// validate left-compare-right

    }

    @Override
    public IRxFun[] left() {
        return listLeft.toArray();
    }

    @Override
    public COMPARE compare() {
        return compare;
    }

    @Override
    public IRxFun[] right() {
        return listRight.toArray();
    }
}
