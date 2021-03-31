package langdef.util;

import langdef.*;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import textevent.impl.TextEventTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static langdef.CMD.POP;
import static langdef.CMD.PUSH;
import static langdef.STRUCT_KEYWORD.INCLUDE;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.LANG_S;

/** Keeping hard-coded lang def names in langDef package, this class exports
 * lists by interface type or by any structure needed */
public class EnumsByType {
    private static EnumsByType instance;

    public static EnumsByType initInstance(){
        return (instance == null)? (instance = new EnumsByType()): instance;
    }

    private EnumsByType(){}

    private Map<String, TextEventTemplate> map;

    private void initMap(){
        map = new HashMap<>();
        String pushSymbol, popSymbol;

        // push symbol = ENUM_NAME, pop symbol = END_ENUM_NAME
        for(LANG_STRUCT langStruct : STRUCT_KEYWORD.values()){
            if((pushSymbol = langStruct.getPushSymbol()) != null){
                map.put(pushSymbol, new TextEventTemplate(langStruct, PUSH, false));
            }
            if((popSymbol = langStruct.getPopSymbol()) != null){
                map.put(popSymbol, new TextEventTemplate(langStruct, POP, false));
            }
        }

        // push symbol = LIST<TYPE>, pop symbol = END_LIST<TYPE>
        for(LANG_STRUCT langStruct : STRUCT_LIST_TYPE.values()){
            if((pushSymbol = langStruct.getPushSymbol()) != null){
                map.put(pushSymbol, new TextEventTemplate(langStruct, PUSH, false));
            }
            if((popSymbol = langStruct.getPopSymbol()) != null){
                map.put(popSymbol, new TextEventTemplate(langStruct, POP, false));
            }
        }

        // symbol != enum name
        for(LANG_STRUCT langStruct : STRUCT_SYMBOL.values()){
            if((pushSymbol = langStruct.getPushSymbol()) != null){
                map.put(pushSymbol, new TextEventTemplate(langStruct, PUSH, false));
            }
            if((popSymbol = langStruct.getPopSymbol()) != null){
                map.put(popSymbol, new TextEventTemplate(langStruct, POP, false));
            }
        }

        // symbol is text prefix; identifier is text substring; no pop symbol
        for(LANG_STRUCT langStruct : STRUCT_LOOKUP.values()){
            if((pushSymbol = langStruct.getPushSymbol()) != null){
                map.put(pushSymbol, new TextEventTemplate(langStruct, PUSH, true));
            }
        }
    }

    public Map<String, TextEventTemplate> langStructEventMapForPatternMatch(){
        if(map == null){
            this.initMap();
        }
        return map;
    }

    public ArrayList<LANG_STRUCT> allFrontEndLangStruct(){
        ArrayList<LANG_STRUCT> list = new ArrayList<>();

        Collections.addAll(list, STRUCT_KEYWORD.values());
        Collections.addAll(list, STRUCT_SYMBOL.values());
        Collections.addAll(list, STRUCT_LOOKUP.values());

        return list;
    }

    public ArrayList<EnumPOJOJoin> allEnumAlgoJoin(){
        ArrayList<EnumPOJOJoin> list = new ArrayList<>();

        Collections.addAll(list, STRUCT_KEYWORD.values());
        Collections.addAll(list, STRUCT_SYMBOL.values());
        Collections.addAll(list, STRUCT_LOOKUP.values());
        Collections.addAll(list, STRUCT_NON_KEYWORD.values());

        return list;
    }

    public ArrayList<LANG_STRUCT> allLangStructures(){
        ArrayList<LANG_STRUCT> list = new ArrayList<>();

        Collections.addAll(list, STRUCT_KEYWORD.values());
        Collections.addAll(list, STRUCT_SYMBOL.values());
        Collections.addAll(list, STRUCT_LOOKUP.values());
        Collections.addAll(list, STRUCT_NON_KEYWORD.values());

        return list;
    }

    public LANG_STRUCT sourceFluidLangStruct(){
        return INCLUDE;
    }
    public LANG_STRUCT sourceAccessLangStruct(){
        return ID_ACCESS;
    }
    public LANG_STRUCT sourceNonCommentLangStruct(){
        return COMMENT;
    }
    public LANG_STRUCT sourceTextPatternIdDefine(){
        return ID_DEFINE;
    }
    public LANG_STRUCT targetLangEnum(){
        return LANG_T;
    }
    public LANG_STRUCT sourceLangEnum(){
        return LANG_S;
    }
    public LANG_STRUCT langRootEnum1(){
        return LANG_ROOT_1;
    }
    public LANG_STRUCT langRootEnum2(){
        return LANG_ROOT_2;
    }


}
