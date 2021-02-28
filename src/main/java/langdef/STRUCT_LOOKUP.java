package langdef;

import langdef.iface.LANG_STRUCT;
import langdef.iface.TEXT_PATTERN;

import static langdef.LangConstants.*;

public enum STRUCT_LOOKUP  implements TEXT_PATTERN, LANG_STRUCT {
    ID_DEFINE (DEFINE_START),
    ID_ACCESS(ACCESS_START),
    COMMENT(COMMENT_START)
    ;

    private final String oSymbol;

    STRUCT_LOOKUP(char oSymbol) {
        this.oSymbol = String.valueOf(oSymbol);
    }

    @Override
    public String getOpenSymbol() {
        return this.oSymbol;
    }

    @Override
    public String getCloseSymbol() {
        return null;
    }

    @Override
    public boolean useSubstring() {
        return true;
    }
}
