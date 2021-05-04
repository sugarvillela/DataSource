package langdefsubalgo.factory;

import langdef.LangConstants;
import langdefsub.WordTraitRuleByStep;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunBuildUtil;
import rule_wordtrait.WordTraitRule;
import rule_wordtrait.impl.WordTraitClientBase;
import runstate.Glob;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;

public abstract class FunBuildUtilBase  extends WordTraitClientBase implements IFunBuildUtil {

    protected static final ITokenizer TOK_ON_SEP = Tokenizer.builder().delimiters(LangConstants.PATH_TREE_SEP).
            skipSymbols("('").keepSkipSymbol().build();

    protected final IBuilderFun builderFun;
    protected final IBuilderNonFun builderNonFun;
    protected final WordTraitRule funTypeRule;
    protected final String pathTreeSep;

    public FunBuildUtilBase(IBuilderFun builderFun, IBuilderNonFun builderNonFun) {
        this.builderFun = builderFun;
        this.builderNonFun = builderNonFun;
        funTypeRule = WordTraitRuleByStep.initInstance().getStep_funType();
        pathTreeSep = String.valueOf(LangConstants.PATH_TREE_SEP);
    }

    protected final ArrayList<IFun> buildList(IFun prev, String text){
        ArrayList<String> outer = TOK_ON_SEP.setText(text).parse().toList();
        ArrayList<String> acc = new ArrayList<>();
        ArrayList<IFun> out = new ArrayList<>();

        IFun curr;
        for(String outerItem : outer){
            Glob.ERR.check(funTypeRule.tryParse(this, outerItem));
            //System.out.println(outerItem + ": type = " + parTypeEnum);
            switch(parTypeEnum){
                case NO:
                    acc.add(outerItem);
                    break;
                case YES:
                    if(!acc.isEmpty()){
                        curr = builderNonFun.build(prev, String.join(pathTreeSep, acc), acc);
                        out.add(curr);
                        prev = curr;
                        acc = new ArrayList<>();
                    }
                    curr = builderFun.build(prev, strings[0], strings[1]);
                    out.add(curr); // strings array has fun name and param list
                    prev = curr;
                    break;
                default:
                    System.out.println(outerItem + ": reached default" );
                    //Glob.ERR.kill(ERR_TYPE.SYNTAX);
            }
        }

        if(!acc.isEmpty()){
            out.add(builderNonFun.build(prev, String.join(pathTreeSep, acc), acc));
        }
        return out;
    }

    interface IBuilderFun{
        IFun build(IFun prev, String funName, String paramList);
    }
    interface IBuilderNonFun{
        IFun build(IFun prev, String text, ArrayList<String> acc);
    }
}
