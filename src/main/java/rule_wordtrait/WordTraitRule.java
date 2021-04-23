package rule_wordtrait;

import err.ERR_TYPE;
import rule_wordtrait.iface.IWordTraitClient;
import rule_wordtrait.iface.ITraitPatternMatch;


public class WordTraitRule  {//implements IWordTraitRule
    private ITraitPatternMatch[] rules;

    public WordTraitRule() {

    }

    public void initWordTraitRule(ITraitPatternMatch... rules) {
        this.rules = rules;
    }

    public ERR_TYPE tryParse(IWordTraitClient client, String text) {
        for(ITraitPatternMatch rule : rules){
            if(rule.tryParse(client, text)){
                return ERR_TYPE.NONE;
            }
        }
        return ERR_TYPE.UNKNOWN_PATTERN;
    }
}