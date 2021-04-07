package langdef;

import java.util.regex.Pattern;

import static langdef.LangConstants.*;
import static runstate.Glob.TOK_SPECIAL;

/** Keeping all language definitions in lang def package.
 *  This class initializes regexes for TokSpecial ProtectPatterns
 *  to differentiate sub-language tokens from main language tokens */
public class TokSpecialPatternInit {
    public void initPatterns(){
        String funFormat = "([^%s%s%s%s]+([%s][0-9A-Za-z_:.]*[%s]))";
        String rangeFormat = "([^%s%s%s%s]+([%s][0-9:]+[%s]))";

        TOK_SPECIAL.initPatterns(
            Pattern.compile(String.format(
                funFormat,  ITEM_OPEN, ITEM_CLOSE, COND_OPEN, COND_CLOSE, COND_OPEN, COND_CLOSE
            )),
            Pattern.compile(String.format(
                rangeFormat, ITEM_OPEN, ITEM_CLOSE, COND_OPEN, COND_CLOSE, ITEM_OPEN, ITEM_CLOSE
            ))
        );
    }
}
