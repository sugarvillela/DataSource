package rule_operator.impl;

import rule_operator.iface.IActionPattern;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionPatternBase implements IActionPattern {
    protected Pattern pattern;
    protected Matcher matcher;
    protected int[] groups;

    @Override
    public IActionPattern setGroup(int... groups) {
        this.groups = groups;
        return this;
    }

    @Override
    public boolean shouldDoAction(String text) {
        matcher = pattern.matcher(text);
        return matcher.find();
    }

    @Override
    public ArrayList<String> doListAction() {
        return null;
    }
}
