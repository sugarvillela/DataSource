package langdef.util;

import langdef.STRUCT_KEYWORD;
import langdef.iface.LANG_STRUCT;

public class LangStructUtil {
    private static LangStructUtil instance;

    public static LangStructUtil initInstance(){
        return (instance == null)? (instance = new LangStructUtil()): instance;
    }

    private LangStructUtil(){}

    LANG_STRUCT lastMatch;

    public boolean isLangStruct(String text){
        return (lastMatch = STRUCT_KEYWORD.getEnum(text)) != null;
    }
    public LANG_STRUCT getLastMatch(){
        return lastMatch;
    }
}
