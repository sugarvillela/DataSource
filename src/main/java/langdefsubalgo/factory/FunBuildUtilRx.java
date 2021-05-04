package langdefsubalgo.factory;

import err.ERR_TYPE;
import generictree.iface.IGTreeNode;
import langdef.util.ListTypeSearch;
import langdefsub.FUN_TYPE;
import langdefsub.WordTraitRuleByStep;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunBuildUtil;
import langdefsubalgo.impl.FunImplGroup;
import readnode.iface.IReadNode;
import rule_wordtrait.WordTraitRule;
import rule_wordtrait.impl.WordTraitClientBase;
import runstate.Glob;

import java.util.ArrayList;

import static langdefsubalgo.impl.FunImplGroup.*;

/** Identify dot-separated path or function and populate list */
public class FunBuildUtilRx extends FunBuildUtilBase {
    private static IFunBuildUtil instance;

    public static IFunBuildUtil initInstance(){
        return (instance == null)? (instance = new FunBuildUtilRx()): instance;
    }

    private FunBuildUtilRx() {
        super(
                new BuilderFunRx(WordTraitRuleByStep.initInstance().getStep_paramType()),
                new BuilderNonFunRx(WordTraitRuleByStep.initInstance().getStep_paramType())
        );
    }

    @Override
    public ArrayList<IFun> build(String text){
        return buildList(null, text);
    }

    private static class BuilderFunRx extends WordTraitClientBase implements IBuilderFun {
        private final WordTraitRule paramTypeRule;

        public BuilderFunRx(WordTraitRule paramTypeRule) {
            this.paramTypeRule = paramTypeRule;
        }

        @Override
        public IFun build(IFun prev, String funName, String paramList){
            this.findParameters(paramList);
            return this.findFunType(prev, funName);
        }
        public IFun findFunType(IFun prev, String text){
            FUN_TYPE funType = FUN_TYPE.fromString(text);
            if(funType != null){
                switch(funType){
                    case RANGE:
                        return new FunImplGroup.FunRange(prev, parTypeEnum, numbers);
                    case LEN:
                        return new FunImplGroup.FunLen(prev, parTypeEnum, strings);
                }
            }

            Glob.ERR.kill(ERR_TYPE.UNKNOWN_FUNCTION);
            return null;
        }

        void findParameters(String text){
            Glob.ERR.check(paramTypeRule.tryParse(this, text));
        }
    }

    private static class BuilderNonFunRx extends WordTraitClientBase implements IBuilderNonFun {
        private final WordTraitRule paramTypeRule;

        public BuilderNonFunRx(WordTraitRule paramTypeRule) {
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
                    return new LiteralObject(prev, this.strings);
                case NUM_PAR:
                    return new LiteralNumber(prev, this.numbers);
                case BOOL_PAR:
                    return new LiteralBool(prev, this.numbers);
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

        IFun getValidFunPathObject(IFun prev, ArrayList<String> acc){
            String[] givenPath = acc.toArray(new String[0]);
            ListTypeSearch search = Glob.LIST_TYPE_SEARCH;
            //System.out.println("validFunPathObject: " + Arrays.toString(givenPath));
            Glob.ERR.check(search.treeNodeFromPartialPath(givenPath));
            IGTreeNode<IReadNode> treeNode = search.treeNodeFound();
            return new FunImplGroup.FunGetPath(prev, search.listTypeFound(), search.pathFound(), treeNode);
        }
    }
}
