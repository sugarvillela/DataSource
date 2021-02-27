package langdef;

import langdef.iface.LANG_STRUCT;

public enum STRUCT_KEYWORD implements LANG_STRUCT {
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
}
