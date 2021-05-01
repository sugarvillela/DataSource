package langdefsubalgo.implfx;

import err.ERR_TYPE;
import langdefsub.COMPARE;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunList;
import langdefsubalgo.iface.IFunPattern;
import runstate.Glob;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;
import java.util.List;

import static langdef.LangConstants.ACCESS_MOD;

public class FxFunPattern implements IFunPattern {
    private static final ITokenizer TOK_ON_BRACKET = Tokenizer.builder().delimiters('[', ']').
            skipSymbols("'(").keepSkipSymbol().keepEscapeSymbol().build();
    private IFunList access, right;

    public FxFunPattern(String text){
        if(text.charAt(0) == ACCESS_MOD){
            this.splitOnBracket(text.substring(1), true);
        }
        else{
            this.splitOnBracket(text, false);
        }
    }

    private void splitOnBracket(String text, boolean accessMod){
        String textLeft, textRight;
        ArrayList<String> tok = TOK_ON_BRACKET.setText(text).parse().toList();

        if(tok.size() != 2){
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
        }

        textLeft = tok.get(0);
        textRight = tok.get(1);

        System.out.println("===FxFunPattern===");
        System.out.println(textLeft);
        System.out.println(textRight);
        System.out.println("======");

        access = new FxAccess(textLeft, accessMod);
        right = new FxFunList(textRight);
    }

    @Override
    public List<IFun> left() {
        return access.toList();
    }

    @Override
    public COMPARE compare() {
        return null;
    }

    @Override
    public List<IFun> right() {
        return right.toList();
    }

    @Override
    public String toString() {
        return "FxFunPattern{" +
                "\n    " + access +
                "\n    " + right +
                "\n}";
    }
}
