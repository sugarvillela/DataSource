package langdef;

import rule_operator.impl.ActionPatternBase;
import static rule_operator.impl.ActionPatternImplGroup.*;
import rule_operator.impl.PatternRule;

import static langdef.LangConstants.*;
import static runstate.Glob.OPERATOR_RULE;

public class RulesByOperatorType {
    public void initRules(){
        OPERATOR_RULE.initPatternRules(
            new PatternRule().
                initPatterns(
                        new StripOuter(ITEM_OPEN, ITEM_CLOSE),
                        new StripOuter(COND_OPEN, COND_CLOSE),
                        new StripOuter(ITEM_CLOSE, COND_OPEN),
                        new StripOuter(ITEM_OPEN, ITEM_CLOSE)

                )

        );
        //SECTION_OPEN.initPatterns(new ActionPatternBase("", SECTION_OPEN.operatorText()));
        //SECTION_OPEN.initAntiPatterns();
    }
}
