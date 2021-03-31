package rule_operator.impl;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static langdef.LangConstants.*;

public abstract class ActionPatternImplGroup {
    public static class StripOuter extends ActionPatternBase {
        private final String outer1, outer2;
        public StripOuter(String... arg) {
            outer1 = arg[0];
            outer2 = arg[0];
            String format = "(^[%s])([^%s%s%s%s]*)([%s]$)";
            this.pattern = Pattern.compile(String.format(format, outer1, ITEM_OPEN, COND_OPEN, COND_CLOSE, ITEM_CLOSE, outer2));//2
        }

        @Override
        public ArrayList<String> doListAction() {
            ArrayList<String> out = new ArrayList<>(3);
            out.add(outer1);
            out.add(matcher.group(2));
            out.add(outer2);
            return out;
        }
    }
}
