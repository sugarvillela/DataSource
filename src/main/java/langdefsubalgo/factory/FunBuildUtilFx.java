package langdefsubalgo.factory;

import err.ERR_TYPE;
import generictree.iface.IGTreeNode;
import langdef.util.ListTypeSearch;
import langdefsub.FUN_TYPE;
import langdefsub.WordTraitRuleByStep;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunBuildUtil;
import langdefsubalgo.impl.FunImplGroup;
import langdefsubalgo.implfx.FxAccess;
import readnode.iface.IReadNode;
import rule_wordtrait.WordTraitRule;
import rule_wordtrait.impl.WordTraitClientBase;
import runstate.Glob;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;

import java.util.ArrayList;

import static langdef.LangConstants.ACCESS_MOD;

public class FunBuildUtilFx extends FunBuildUtilBase {
    private static final ITokenizer TOK_ON_BRACKET = Tokenizer.builder().delimiters('[', ']').
            skipSymbols("'(").keepSkipSymbol().keepEscapeSymbol().build();

    private static IFunBuildUtil instance;

    public static IFunBuildUtil initInstance(){
        return (instance == null)? (instance = new FunBuildUtilFx()): instance;
    }

    private FunBuildUtilFx() {
        super(
                new BuilderFunFx(WordTraitRuleByStep.initInstance().getStep_paramType()),
                new BuilderNonFunFx(WordTraitRuleByStep.initInstance().getStep_paramType())
        );
    }

    @Override
    public ArrayList<IFun> build(String text){
        String textLeft, textRight;
        ArrayList<String> tok;
        boolean accessMod;

        if(text.charAt(0) == ACCESS_MOD){
            tok = TOK_ON_BRACKET.setText(text.substring(1)).parse().toList();
            accessMod = true;
        }
        else{
            tok = TOK_ON_BRACKET.setText(text).parse().toList();
            accessMod = false;
        }

        if(tok.size() != 2){
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
        }

        textLeft = tok.get(0);
        textRight = tok.get(1);

        System.out.println("===FunBuildUtilFx===");
        System.out.println(textLeft);
        System.out.println(textRight);
        System.out.println("======");

        IFun access = new FxAccess(null, textLeft, accessMod);
        ArrayList<IFun> out = new ArrayList<>();
        out.add(access);
        out.addAll(buildList(access, textRight));

        return out;
    }

    private static class BuilderFunFx extends WordTraitClientBase implements FunBuildUtilBase.IBuilderFun {
        private final WordTraitRule paramTypeRule;

        public BuilderFunFx(WordTraitRule paramTypeRule) {
            this.paramTypeRule = paramTypeRule;
        }

        @Override
        public IFun build(IFun prev, String funName, String paramList){
            this.findParameters(paramList);
            return this.findFunType(prev, funName);
        }
        private IFun findFunType(IFun prev, String text){
            FUN_TYPE funType = FUN_TYPE.fromString(text);
            if(funType != null){
                switch(funType){
                    case VOTE:  // PATH -> VOTE -> ? param [EMPTY] Vote for a flag item
                        return new FunImplGroup.FunVote(prev, parTypeEnum);
                    case SET:   // PATH -> SET -> ? param [EMPTY|BOOL_PAR|NUMBER|STRING]  Set a flag item ... paramType == listType
                        return new FunImplGroup.FunSet(prev, parTypeEnum);
                    case DROP:  // PATH -> DROP -> ? param [EMPTY] Drop a flag item
                        return new FunImplGroup.FunDrop(prev, parTypeEnum);
                    case MOVE:  // [num, numList, range] -> MOVE -> ? param [num] Structure: move a word
                    case COPY:  // [num, numList, range] -> PUT -> ? param [num]  Structure: new word with same attrib as accessed
//                    case PUT:   // ACCESS -> PUT -> ? param [num, numList, range]
//                        return new FunImplGroup.FunPut(parTypeEnum, strings);
                    case SWAP:
                        return new FunImplGroup.FunSwap(prev, parTypeEnum);
                    case CON:   // NULL -> PUT -> ? param [id, path]
                        return new FunImplGroup.FunCon(prev, parTypeEnum);
                }
            }
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_FUNCTION);
            return null;
        }

        void findParameters(String text){
            Glob.ERR.check(paramTypeRule.tryParse(this, text));
        }
    }

    private static class BuilderNonFunFx extends WordTraitClientBase implements FunBuildUtilBase.IBuilderNonFun {
        private final WordTraitRule paramTypeRule;

        public BuilderNonFunFx(WordTraitRule paramTypeRule) {
            this.paramTypeRule = paramTypeRule;
        }

        @Override
        public IFun build(IFun prev, String text, ArrayList<String> acc){//id(not quoted), path(dot sep), literal, quoted, !quotedList
            Glob.ERR.check(paramTypeRule.tryParse(this, text));
            switch(this.parTypeEnum){
                case ID_PAR:    // string (not quoted)
                {
                    IFun funPath = this.getValidFunPathObject(prev, acc);
                    if(funPath != null){
                        return funPath;
                    }
                }   // falls through if not valid path
                case STR_PAR:   // string (quoted)
                    return new FunImplGroup.LiteralObject(prev, this.strings);
                case NUM_PAR:
                    return new FunImplGroup.LiteralNumber(prev, this.numbers);
                case BOOL_PAR:
                    return new FunImplGroup.LiteralBool(prev, this.numbers);
                case ID_PATH:
                {
                    IFun funPath = this.getValidFunPathObject(prev, acc);
                    if(funPath != null){
                        return funPath;
                    }
                }   // falls through if not valid path
                default:
                    Glob.ERR.kill(ERR_TYPE.SYNTAX);
                    return null;
            }
        }

        private IFun getValidFunPathObject(IFun prev, ArrayList<String> acc){
            String[] givenPath = acc.toArray(new String[0]);
            ListTypeSearch search = Glob.LIST_TYPE_SEARCH;
            //System.out.println("validFunPathObject");
            Glob.ERR.check(search.treeNodeFromPartialPath(givenPath));
            IGTreeNode<IReadNode> treeNode = search.treeNodeFound();
            return new FunImplGroup.FunGetPath(prev, search.listTypeFound(), search.pathFound(), treeNode);
        }
    }
}
