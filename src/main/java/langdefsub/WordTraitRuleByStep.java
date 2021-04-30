package langdefsub;

import rule_wordtrait.WordTraitRule;
import rule_wordtrait.iface.ICharTraitParser;
import rule_wordtrait.impl.CharTraitImplGroup;
import rule_wordtrait.impl.CharTraitParser;
import rule_wordtrait.impl.TraitPatternMatch;
import rule_wordtrait.impl.WordTraitActionImplGroup;

import static langdefsub.PAR_TYPE.*;

public class WordTraitRuleByStep {
    private static WordTraitRuleByStep instance;

    public static WordTraitRuleByStep initInstance(){
        return (instance == null)? (instance = new WordTraitRuleByStep()): instance;
    }

    private WordTraitRule step_funType, step_paramType;
    private WordTraitRuleByStep(){}

    public WordTraitRule getStep_wordRange(){
        ICharTraitParser parser = CharTraitParser.builder().
                skipSymbols("'{").keepEscapeSymbol().
                traits(
                        new CharTraitImplGroup.CharTrait('{'),
                        new CharTraitImplGroup.CharTrait('}'),
                        new CharTraitImplGroup.CharTraitVisibleAscii('C')
                ).build();
        WordTraitRule rule = new WordTraitRule();
        rule.initWordTraitRule(
                new TraitPatternMatch(// WordTraitActionNumRange.initInstance(':')
                        parser, "+C+{+}", YES, WordTraitActionImplGroup.ActionOnRxWordRangeYes.initInstance('{', '}')
                ),
                new TraitPatternMatch(
                        parser, "+C-{-}", NO, WordTraitActionImplGroup.ActionOnNo.initInstance()
                )
        );
        return rule;
    }

    // detect FUNCTION(PAR) vs IDENTIFIER by presence of ()
    public WordTraitRule getStep_funType(){
        if(step_funType != null){
            return step_funType;
        }
        ICharTraitParser parser = CharTraitParser.builder().
                skipSymbols("'(").keepEscapeSymbol().
                traits(
                        new CharTraitImplGroup.CharTrait('('),
                        new CharTraitImplGroup.CharTrait(')'),
                        new CharTraitImplGroup.CharTraitVisibleAscii('C')
                ).build();
        step_funType = new WordTraitRule();
        step_funType.initWordTraitRule(
                new TraitPatternMatch(
                        parser, "+C+(+)", YES, WordTraitActionImplGroup.ActionOnFunParYes.initInstance('(', ')')
                ),
                new TraitPatternMatch(
                        parser, "+C-(-)", NO, WordTraitActionImplGroup.ActionOnNo.initInstance()
                )
        );
        return step_funType;
    }
    public WordTraitRule getStep_paramType(){
        if(step_paramType != null){
            return step_paramType;
        }
        ICharTraitParser parser = CharTraitParser.builder().
                skipSymbols('\'').keepEscapeSymbol().
                traits(
                        new CharTraitImplGroup.CharTrait('\''),
                        new CharTraitImplGroup.CharTrait('.'),
                        new CharTraitImplGroup.CharTrait(','),
                        new CharTraitImplGroup.CharTrait(':'),
                        new CharTraitImplGroup.CharTrait('*'),
                        new CharTraitImplGroup.CharTrait('['),
                        new CharTraitImplGroup.CharTrait('_'),
                        new CharTraitImplGroup.CharTraitAlpha('A'),
                        new CharTraitImplGroup.CharTraitVisibleAscii('C'),
                        new CharTraitImplGroup.CharTraitNumeric('N')
                ).build();

        step_paramType = new WordTraitRule();
        step_paramType.initWordTraitRule(
                new TraitPatternMatch(
                        parser, "-'-*-,-.-:-A+C+N-[-_", NUM_PAR, WordTraitActionImplGroup.ActionNumber.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*+,-.-:-A+C+N-[-_", NUM_LIST, WordTraitActionImplGroup.ActionNumbers.initInstance(',')
                ),
                new TraitPatternMatch(
                        parser, "+'-*-,-.-:-A+C-N-[-_", STR_PAR, WordTraitActionImplGroup.ActionQuotedString.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "+'-*+,-.-:-A+C-N-[-_", STR_LIST, WordTraitActionImplGroup.ActionQuotedStringList.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*-,-.-:+A+C.N-[._", ID_PAR, WordTraitActionImplGroup.ActionStringId.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*+,-.-:+A+C.N-[._", ID_LIST, WordTraitActionImplGroup.ActionStringIds.initInstance(',')
                ),
                new TraitPatternMatch(
                        parser, "-'-*-,+.-:+A+C.N-[._", ID_PATH, WordTraitActionImplGroup.ActionStringIds.initInstance('.')
                ),
                new TraitPatternMatch(
                        parser, "-'+*-,-.-:+A+C.N-[._", STAR_ID, WordTraitActionImplGroup.ActionStringId.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*-,-.+:-A+C+N-[-_", NUM_RANGE, WordTraitActionImplGroup.ActionNumRange.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'+*-,-.-:-A+C+N+[-_", STAR_NUM, WordTraitActionImplGroup.ActionNumber.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'+*-,-.+:-A+C+N+[-_", STAR_RANGE, WordTraitActionImplGroup.ActionNumbers.initInstance(':')
                )
        );
        return step_paramType;
    }
}
