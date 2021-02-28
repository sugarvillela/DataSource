package langdef;

import langdef.iface.LANG_STRUCT;
import langdef.iface.TEXT_PATTERN;

public enum STRUCT_KEYWORD implements TEXT_PATTERN, LANG_STRUCT {
    INSERT,
    RX,
    FX
    ;

    public static STRUCT_KEYWORD getEnum(String text){
        try{
            return valueOf(text);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public String getOpenSymbol() {
        return this.toString();
    }

    @Override
    public String getCloseSymbol() {
        return null;
    }

    @Override
    public boolean useSubstring() {
        return false;
    }
}
