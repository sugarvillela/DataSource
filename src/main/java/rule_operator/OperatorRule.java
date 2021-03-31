package rule_operator;

import rule_operator.iface.IPatternRule;

import java.util.ArrayList;

public class OperatorRule {
    private static OperatorRule instance;

    public static OperatorRule initInstance(){
        return (instance == null)? (instance = new OperatorRule()): instance;
    }

    private OperatorRule(){}

    IPatternRule[] patternRules;
    public void initPatternRules(IPatternRule... patternRules){
        this.patternRules = patternRules;
    }
    public ArrayList<String> doListAction(String text){
        for(IPatternRule patternRule : patternRules){
            if(patternRule.shouldDoAction(text)){
                return patternRule.doListAction(text);
            }
        }
        return null;
    }
}
