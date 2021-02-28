package langdef;

import langdef.iface.LANG_STRUCT;
import langdef.iface.TEXT_PATTERN;

import static langdef.LangConstants.*;

public enum STRUCT_SYMBOL implements TEXT_PATTERN, LANG_STRUCT {
    LANG_S(OPEN_S, CLOSE_S),
    LANG_T(OPEN_T, CLOSE_T),
    FUN(FUNCTION, END_FUNCTION),
    ;

    private final String oSymbol, cSymbol;

    STRUCT_SYMBOL(String oSymbol, String cSymbol) {
        this.oSymbol = oSymbol;
        this.cSymbol = cSymbol;
    }

    @Override
    public String getOpenSymbol() {
        return this.oSymbol;
    }

    @Override
    public String getCloseSymbol() {
        return this.cSymbol;
    }

    @Override
    public boolean useSubstring() {
        return false;
    }
}
