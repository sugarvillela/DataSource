package langdefsubalgo.implrx;

import err.ERR_TYPE;
import langdefsub.PAR_TYPE;
import langdefsub.WordTraitRuleByStep;
import langdefsubalgo.util.RangeUtil;
import langdefsubalgo.validators.ValidFunList;
import rule_wordtrait.WordTraitRule;
import rule_wordtrait.iface.IWordTraitClient;
import runstate.Glob;
import langdefsub.COMPARE;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunList;
import langdefsubalgo.iface.IFunPattern;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RxFunPattern implements IFunPattern, IWordTraitClient {
    private static final ITokenizer TOK_ON_COMPARE = Tokenizer.builder().delimiters(COMPARE.allChars()).
            skipSymbols("'(").tokenizeDelimiter().keepSkipSymbol().keepEscapeSymbol().build();

    private COMPARE compare;
    private IFunList listLeft, listRight;
    private String[] strings;
    private int[] numbers;
    private PAR_TYPE parTypeEnum;

    public RxFunPattern(String text) {
        if(this.setRangeFromSymbol(text)){  // parse end symbols *+? if any
            text = text.substring(0, text.length() - 1);
        }
        this.setRangeFromCurly(text);       // parse range if any (err if both *+? and range)
        if(numbers == null){
            this.receiveContent(text);
            this.receiveContent(1, 1);
        }

        this.splitOnCompare();              // separate left and right
        ValidFunList.initInstance().validCompare(listLeft, compare, listRight);
    }
    private boolean setRangeFromSymbol(String text){
        RangeUtil rangeUtil = RangeUtil.initInstance();
        return rangeUtil.translateSymbolToRange(this, text);
    }
    private void setRangeFromCurly(String text){
        WordTraitRule wordTraitRule = WordTraitRuleByStep.initInstance().getStep_wordRange();
        Glob.ERR.check(wordTraitRule.tryParse(this, text));
    }
    private void splitOnCompare(){
        String text = strings[0];
        String textLeft, textRight;
        ArrayList<String> tok = TOK_ON_COMPARE.setText(text).parse().toList();

        if(tok.size() != 3){
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
        }

        textLeft = tok.get(0);
        compare = COMPARE.fromChar(tok.get(1).charAt(0));
        textRight = tok.get(2);

//        System.out.println("===RxFunPattern===");
//        System.out.println(textLeft);
//        System.out.println(compare);
//        System.out.println(textRight);
//        System.out.println("======");

        listLeft = new RxFunList(textLeft);
        listRight = new RxFunList(textRight);
    }

    @Override
    public List<IFun> left() {
        return listLeft.toList();
    }

    @Override
    public COMPARE compare() {
        return compare;
    }

    @Override
    public List<IFun> right() {
        return listRight.toList();
    }

    /*=====IWordTraitClient methods: only need range==================================================================*/

    @Override
    public void receiveContent(String... content) {
        strings = content;
    }

    @Override
    public void receiveContent(int... content) {
        if(numbers != null){
            Glob.ERR.kill("Duplicate range. Use *, +, ? or {a:b} exclusively");
        }
        numbers = content;
    }

    @Override
    public void receiveContent(PAR_TYPE content) {}

    @Override
    public String toString() {
        return
                "strings=" + Arrays.toString(strings) +
                        "|numbers=" + Arrays.toString(numbers);
    }
}
