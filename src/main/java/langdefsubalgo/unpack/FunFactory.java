package langdefsubalgo.unpack;

import langdef.LangConstants;
import langdefsubalgo.iface.IRxFun;
import langdefsubalgo.impl.RxData;
import langdefsubalgo.impl.RxFun;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;

public class FunFactory {
    private static final ITokenizer TOK_ON_SEP = Tokenizer.builder().delimiters(LangConstants.PATH_TREE_SEP).
            skipSymbols("('").keepSkipSymbol().build();

    private static final ITokenizer TOK_ON_PAR = Tokenizer.builder().delimiters('(', ')').//, ')'
            skipSymbols('\'').keepSkipSymbol().tokenizeDelimiter().build();

    public ArrayList<IRxFun> buildList(String text){
        ArrayList<String> outer = TOK_ON_SEP.setText(text).parse().toList();
        ArrayList<String> acc = new ArrayList<>();
        ArrayList<IRxFun> out = new ArrayList<>();

        for(String outerItem : outer){
            ArrayList<String> inner = TOK_ON_PAR.setText(outerItem).parse().toList();
            switch(inner.size()){
                case 1:
                    acc.add(outerItem);
                    break;
                case 3:
                case 4:
                    if(!acc.isEmpty()){
                        out.add(new RxData(acc));
                        acc = new ArrayList<>();
                    }
                    out.add(new RxFun(inner));
                    break;
                default:
                    System.out.println(outerItem + " inner = " + String.join("|", inner));
                    //Glob.ERR.kill(ERR_TYPE.SYNTAX);
            }
        }

        if(!acc.isEmpty()){
            out.add(new RxData(acc));
        }
        return out;
    }
}
