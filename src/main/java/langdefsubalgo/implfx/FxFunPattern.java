package langdefsubalgo.implfx;

import err.ERR_TYPE;
import langdefsub.COMPARE;
import langdefsubalgo.factory.FactoryFx;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunList;
import langdefsubalgo.iface.IFunPattern;
import langdefsubalgo.impl.FunList;
import runstate.Glob;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;
import java.util.List;

import static langdef.LangConstants.ACCESS_MOD;

public class FxFunPattern implements IFunPattern {
    private static final ITokenizer TOK_ON_BRACKET = Tokenizer.builder().delimiters('[', ']').
            skipSymbols("'(").keepSkipSymbol().keepEscapeSymbol().build();

    private IFunList access, right, all;

    public FxFunPattern(String text){
        all = new FunList(text, FactoryFx.initInstance());
    }

    private void populate(String text, boolean accessMod){
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

        access = new FxAccessListWrap(textLeft, accessMod);
        right = new FunList(textRight, FactoryFx.initInstance());
    }

    @Override
    public List<IFun> left() {
        List<IFun> out = new ArrayList<>(1);
        out.add(all.toList().get(0));
        return out;
    }

    @Override
    public COMPARE compare() {
        return null;
    }

    @Override
    public List<IFun> right() {
        List<IFun> list = all.toList();
        return new ArrayList<>(list.subList(1, list.size()));
    }

    @Override
    public String toString() {
        return "FxFunPattern\n{\n" +
                all.toString() +
                "\n}";
    }
}
