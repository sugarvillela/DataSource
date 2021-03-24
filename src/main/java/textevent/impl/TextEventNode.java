package textevent.impl;

import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;
import textevent.iface.ITextEventNode;

import java.util.ArrayList;

import static runstate.Glob.NULL_TEXT;

public class TextEventNode implements ITextEventNode {
    private static final String FORMAT_CSV = "%s,%s,%s";
    private final LANG_STRUCT langStruct;
    private final CMD cmd;
    private String substring;

    public TextEventNode(LANG_STRUCT langStruct, CMD cmd) {
        this(langStruct, cmd, null);
    }
    public TextEventNode(LANG_STRUCT langStruct, CMD cmd, String substring) {
        this.langStruct = langStruct;
        this.cmd = cmd;
        this.substring = substring;
    }

    @Override
    public LANG_STRUCT langStruct() {
        return langStruct;
    }

    @Override
    public CMD cmd() {
        return cmd;
    }

    @Override
    public void setSubstring(String substring) {
        this.substring = substring;
        //System.out.println("setSubstring: " + this.csvString());
    }

    @Override
    public boolean hasSubstring() {
        return substring != null;
    }

    @Override
    public String substring() {
        //System.out.println("getSubstring: " + this.csvString());
        return substring;
    }

    @Override
    public String csvString() {
        return String.format(FORMAT_CSV,
                (cmd == null)?          NULL_TEXT : cmd.toString(),
                (langStruct == null)?   NULL_TEXT : langStruct.toString(),
                (substring == null)?    NULL_TEXT : substring
        );
    }

    @Override
    public String friendlyString() {
        ArrayList<String> out = new ArrayList<>();
        if(langStruct != null){ out.add("langStruct: " + langStruct.toString()); }
        if(cmd != null){ out.add("cmd: " + cmd.toString()); }
        if(substring != null){ out.add("substring: " + substring); }
        return String.join(", ", out);
    }
}
