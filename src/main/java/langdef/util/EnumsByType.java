package langdef.util;

import langdef.STRUCT_KEYWORD;
import langdef.STRUCT_LOOKUP;
import langdef.STRUCT_SYMBOL;
import langdef.iface.LANG_STRUCT;
import langdef.iface.TEXT_PATTERN;
import textevent.iface.ITextEventNode;
import textevent.impl.TextEventNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static langdef.CMD.POP;
import static langdef.CMD.PUSH;
import static langdef.STRUCT_KEYWORD.INSERT;
import static langdef.STRUCT_LOOKUP.COMMENT;

/** List static enums by which interface they implement */
public class EnumsByType {
    private static EnumsByType instance;

    public static EnumsByType initInstance(){
        return (instance == null)? (instance = new EnumsByType()): instance;
    }

    private EnumsByType(){}

    private Map<String, ITextEventNode> map;

    private void initMap(){
        map = new HashMap<>();
        for(TEXT_PATTERN textPattern : this.getTextPatterns()){
            String oSymbol = textPattern.getOpenSymbol();
            String cSymbol = textPattern.getCloseSymbol();

            if(oSymbol != null){
                map.put(oSymbol, new TextEventNode(textPattern, PUSH));
            }
            if(cSymbol != null){
                map.put(cSymbol, new TextEventNode(textPattern, POP));
            }
        }
    }

    public Map<String, ITextEventNode> getTextPatternsAsMap(){
        if(map == null){
            this.initMap();
        }
        return map;
    }

    public ArrayList<TEXT_PATTERN> getTextPatterns(){
        ArrayList<TEXT_PATTERN> list = new ArrayList<>();

        Collections.addAll(list, STRUCT_KEYWORD.values());
        Collections.addAll(list, STRUCT_SYMBOL.values());
        Collections.addAll(list, STRUCT_LOOKUP.values());

        return list;
    }

    public ArrayList<LANG_STRUCT> getLangStructures(){
        ArrayList<LANG_STRUCT> list = new ArrayList<>();

        Collections.addAll(list, STRUCT_KEYWORD.values());
        Collections.addAll(list, STRUCT_SYMBOL.values());
        Collections.addAll(list, STRUCT_LOOKUP.values());
        // TODO There are more language structures
        return list;
    }

    public TEXT_PATTERN sourceFluidTextPattern(){
        return INSERT;
    }
    public TEXT_PATTERN sourceNonCommentTextPattern(){
        return COMMENT;
    }
}
