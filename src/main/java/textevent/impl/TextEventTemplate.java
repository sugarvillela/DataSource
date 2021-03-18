package textevent.impl;

import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;
import textevent.iface.ITextEventTemplate;

public class TextEventTemplate implements ITextEventTemplate {
    private final LANG_STRUCT langStruct;
    private final CMD cmd;
    private final boolean useSubstring;

    public TextEventTemplate(LANG_STRUCT langStruct, CMD cmd, boolean useSubstring) {
        this.langStruct = langStruct;
        this.cmd = cmd;
        this.useSubstring = useSubstring;
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
    public boolean useSubstring() {
        return useSubstring;
    }
}
