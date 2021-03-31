package rule_operator.impl;

import rule_operator.iface.IActionPattern;
import rule_operator.iface.IPatternRule;

import java.util.ArrayList;

public class PatternRule implements IPatternRule {
    private IActionPattern[] patterns;
    private IActionPattern[] antiPatterns;
    private IActionPattern selectedPattern;

    @Override
    public IPatternRule initPatterns(IActionPattern... patterns) {
        this.patterns = patterns;
        return this;
    }

    @Override
    public IPatternRule initAntiPatterns(IActionPattern... antiPatterns) {
        this.antiPatterns = antiPatterns;
        return this;
    }

    @Override
    public boolean shouldDoAction(String text) {
        if(antiPatterns != null){
            for(IActionPattern antiPattern : antiPatterns){
                if(!antiPattern.shouldDoAction(text)){
                    selectedPattern = null;
                    return false;
                }
            }
        }
        for(IActionPattern pattern : patterns){
            if(pattern.shouldDoAction(text)){
                selectedPattern = pattern;
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<String> doListAction(String text) {
        if(selectedPattern == null){
            return null;
        }
        return selectedPattern.doListAction();
    }

    @Override
    public String operatorText() {
        return null;
    }

}
