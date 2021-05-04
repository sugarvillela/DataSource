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

    private WordTraitRule step_wordRange, step_funType, step_paramType, step_accessType;
    private WordTraitRuleByStep(){}

    // detect for separation: RX_PATTERN{1:2} to strings=RX_PATTERN, numbers=[1, 2]
    public WordTraitRule getStep_wordRange(){
        if(step_wordRange != null){
            return step_wordRange;
        }
        ICharTraitParser parser = CharTraitParser.builder().
                skipSymbols("'{").keepEscapeSymbol().
                traits(
                        new CharTraitImplGroup.CharTrait('{'),
                        new CharTraitImplGroup.CharTrait('}'),
                        new CharTraitImplGroup.CharTraitVisibleAscii('C')
                ).build();
        step_wordRange = new WordTraitRule();
        step_wordRange.initWordTraitRule(
                new TraitPatternMatch(// WordTraitActionNumRange.initInstance(':')
                        parser, "+C+{+}", YES, WordTraitActionImplGroup.ActionOnRxWordRangeYes.initInstance('{', '}')
                ),
                new TraitPatternMatch(
                        parser, "+C-{-}", NO, WordTraitActionImplGroup.ActionOnNo.initInstance()
                )
        );
        return step_wordRange;
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
                        new CharTraitImplGroup.CharTrait('-'),
                        new CharTraitImplGroup.CharTraitAlpha('A'),
                        new CharTraitImplGroup.CharTraitVisibleAscii('C'),
                        new CharTraitImplGroup.CharTraitNumeric('N')
                ).build();
        System.out.println(parser.getWatchedTraits());
        step_paramType = new WordTraitRule();
        step_paramType.initWordTraitRule(
                new TraitPatternMatch(
                        parser, "-'-*-,---.-:-A+C+N-[-_", NUM_PAR, WordTraitActionImplGroup.ActionNumber.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*-,+--.-:-A+C+N-[-_", NUM_PAR_NEG, WordTraitActionImplGroup.ActionNumber.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*+,---.-:-A+C+N-[-_", NUM_LIST, WordTraitActionImplGroup.ActionNumbers.initInstance(',')
                ),
                new TraitPatternMatch(
                        parser, "-'-*+,+--.-:-A+C+N-[-_", NUM_LIST_NEG, WordTraitActionImplGroup.ActionNumbers.initInstance(',')
                ),
                new TraitPatternMatch(
                        parser, "+'-*-,---.-:-A+C-N-[-_", STR_PAR, WordTraitActionImplGroup.ActionQuotedString.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "+'-*+,---.-:-A+C-N-[-_", STR_LIST, WordTraitActionImplGroup.ActionQuotedStringList.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*-,---.-:+A+C.N-[._", ID_PAR, WordTraitActionImplGroup.ActionStringId.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*+,---.-:+A+C.N-[._", ID_LIST, WordTraitActionImplGroup.ActionStringIds.initInstance(',')
                ),
                new TraitPatternMatch(
                        parser, "-'-*-,--+.-:+A+C.N-[._", ID_PATH, WordTraitActionImplGroup.ActionStringIds.initInstance('.')
                ),
                new TraitPatternMatch(
                        parser, "-'+*-,---.-:+A+C.N-[._", STAR_ID, WordTraitActionImplGroup.ActionStringId.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-'-*-,---.+:-A+C+N-[-_", NUM_RANGE, WordTraitActionImplGroup.ActionNumRange.initInstance()
                )
        );
        return step_paramType;
    }

    public WordTraitRule getStep_accessType(){
        if(step_accessType != null){
            return step_accessType;
        }
        ICharTraitParser parser = CharTraitParser.builder().
                skipSymbols('\'').keepEscapeSymbol().
                traits(
                        new CharTraitImplGroup.CharTrait('>'),//access
                        new CharTraitImplGroup.CharTrait('<'),
                        new CharTraitImplGroup.CharTrait(':'),
                        new CharTraitImplGroup.CharTrait(','),
                        new CharTraitImplGroup.CharTrait('-'),
                        new CharTraitImplGroup.CharTraitAlpha('A'),
                        new CharTraitImplGroup.CharTraitVisibleAscii('C'),
                        new CharTraitImplGroup.CharTraitNumeric('N')
                ).build();
        System.out.println(parser.getWatchedTraits());
        step_accessType = new WordTraitRule();
        step_accessType.initWordTraitRule(
                new TraitPatternMatch(// << first element
                        parser, "-,---:+<->-A+C-N", NUM_PAR, WordTraitActionImplGroup.ActionOverrideTextNumber.initInstance(0)
                ),
                new TraitPatternMatch(// >> last element
                        parser, "-,---:-<+>-A+C-N", NUM_PAR_NEG, WordTraitActionImplGroup.ActionOverrideTextNumber.initInstance(-1)
                ),
                new TraitPatternMatch(// <> all elements
                        parser, "-,---:+<+>-A+C-N", NUM_RANGE, WordTraitActionImplGroup.ActionOverrideTextRange.initInstance(0, 1024)
                ),
                new TraitPatternMatch(
                        parser, "-,--+:-<->-A+C+N", NUM_RANGE, WordTraitActionImplGroup.ActionNumRange.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-,---:-<->-A+C+N", NUM_PAR, WordTraitActionImplGroup.ActionNumber.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "-,+--:-<->-A+C+N", NUM_PAR_NEG, WordTraitActionImplGroup.ActionNumber.initInstance()
                ),
                new TraitPatternMatch(
                        parser, "+,---:-<->-A+C+N", NUM_LIST, WordTraitActionImplGroup.ActionNumbers.initInstance(',')
                )
//                new TraitPatternMatch(
//                        parser, "+,+--:-<->-A+C+N", NUM_LIST_NEG, WordTraitActionImplGroup.ActionNumbers.initInstance(',')
//                )
        );
        return step_accessType;
    }
}
