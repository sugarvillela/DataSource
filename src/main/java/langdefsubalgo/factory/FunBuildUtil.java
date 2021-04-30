package langdefsubalgo.factory;

import err.ERR_TYPE;
import generictree.iface.IGTreeNode;
import langdef.LangConstants;
import langdef.util.ListTypeSearch;
import langdefsub.FUN_TYPE;
import langdefsub.WordTraitRuleByStep;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.implrx.RxFunImplGroup;
import readnode.iface.IReadNode;
import rule_wordtrait.WordTraitRule;
import rule_wordtrait.impl.WordTraitClientBase;
import runstate.Glob;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;

import static langdefsubalgo.implrx.RxFunImplGroup.*;

/** Identify dot-separated path or function and populate list */
public class FunBuildUtil extends WordTraitClientBase {
    private static FunBuildUtil instance;

    public static FunBuildUtil initInstance(){
        return (instance == null)? (instance = new FunBuildUtil()): instance;
    }

    private static final ITokenizer TOK_ON_SEP = Tokenizer.builder().delimiters(LangConstants.PATH_TREE_SEP).
            skipSymbols("('").keepSkipSymbol().build();

    private final WordTraitRule funTypeRule;
    private final BuilderFun builderFun;
    private final BuilderNonFun builderNonFun;
    private final String pathTreeSep;

    public FunBuildUtil() {
        funTypeRule = WordTraitRuleByStep.initInstance().getStep_funType();
        builderFun = new BuilderFun(WordTraitRuleByStep.initInstance().getStep_paramType());
        builderNonFun = new BuilderNonFun(WordTraitRuleByStep.initInstance().getStep_paramType());
        pathTreeSep = String.valueOf(LangConstants.PATH_TREE_SEP);
    }

    public ArrayList<IFun> buildList(String text){
        ArrayList<String> outer = TOK_ON_SEP.setText(text).parse().toList();
        ArrayList<String> acc = new ArrayList<>();
        ArrayList<IFun> out = new ArrayList<>();

        for(String outerItem : outer){
            Glob.ERR.check(funTypeRule.tryParse(this, outerItem));
            //System.out.println(outerItem + ": type = " + parTypeEnum);
            switch(parTypeEnum){
                case NO:
                    acc.add(outerItem);
                    break;
                case YES:
                    if(!acc.isEmpty()){
                        out.add(builderNonFun.build(String.join(pathTreeSep, acc), acc));
                        acc = new ArrayList<>();
                    }
                    out.add(builderFun.build(strings[0], strings[1])); // strings array has fun name and param list
                    break;
                default:
                    System.out.println(outerItem + ": reached default" );
                    //Glob.ERR.kill(ERR_TYPE.SYNTAX);
            }
        }

        if(!acc.isEmpty()){
            out.add(builderNonFun.build(String.join(pathTreeSep, acc), acc));
        }
        return out;
    }

    public static class BuilderFun extends WordTraitClientBase {
        private final WordTraitRule paramTypeRule;

        public BuilderFun(WordTraitRule paramTypeRule) {
            this.paramTypeRule = paramTypeRule;
        }

        IFun build(String funName, String paramList){
            this.findParameters(paramList);
            return this.findFunType(funName);
        }
        IFun findFunType(String text){
            FUN_TYPE funType = FUN_TYPE.fromString(text);
            if(funType != null){
                switch(funType){
                    case RANGE:
                        return new RxFunImplGroup.FunRange(parTypeEnum, numbers);
                    case LEN:
                        return new RxFunImplGroup.FunLen(parTypeEnum, strings);
                }
            }

            Glob.ERR.kill(ERR_TYPE.UNKNOWN_FUNCTION);
            return null;
        }

        void findParameters(String text){
            Glob.ERR.check(paramTypeRule.tryParse(this, text));
        }
    }

    public static class BuilderNonFun extends WordTraitClientBase {
        private final WordTraitRule paramTypeRule;

        public BuilderNonFun(WordTraitRule paramTypeRule) {
            this.paramTypeRule = paramTypeRule;
        }

        IFun build(String text, ArrayList<String> acc){//id(not quoted), path(dot sep), literal, quoted, !quotedList
            Glob.ERR.check(paramTypeRule.tryParse(this, text));
            switch(this.parTypeEnum){
                case ID_PAR:    // string (not quoted)
                {
                    IFun funPath = this.getValidFunPathObject(acc);
                    if(funPath != null){
                        return funPath;
                    }
                }   // falls through if not valid path
                case STR_PAR:   // string (quoted)
                    return new LiteralObject(this.strings);
                case NUM_PAR:
                    return new LiteralNumber(this.numbers);
                case BOOL_PAR:
                    return new LiteralBool(this.numbers);
                case ID_PATH:
                {
                    IFun funPath = this.getValidFunPathObject(acc);
                    if(funPath != null){
                        return funPath;
                    }
                }   // falls through if not valid path
                default:
                    Glob.ERR.kill(ERR_TYPE.SYNTAX);
                    return null;
            }
        }

        IFun getValidFunPathObject(ArrayList<String> acc){
            String[] givenPath = acc.toArray(new String[0]);
            ListTypeSearch search = Glob.LIST_TYPE_SEARCH;
            //System.out.println("validFunPathObject");
            if(search.treeNodeFromPartialPath(givenPath)){
                //System.out.println("if case true");
                IGTreeNode<IReadNode> treeNode = search.treeNodeFound();
                return new FunPath(search.listTypeFound(), search.pathFound(), treeNode);
            }
            //System.out.println("if false");
            return null;
        }
    }
}
