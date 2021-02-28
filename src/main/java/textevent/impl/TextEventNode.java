package textevent.impl;

import langdef.CMD;
import langdef.iface.TEXT_PATTERN;
import textevent.iface.ITextEventNode;

import java.util.ArrayList;

public class TextEventNode implements ITextEventNode {
    private final TEXT_PATTERN textPattern;
    private final CMD cmd;

    public TextEventNode(TEXT_PATTERN textPattern, CMD cmd) {
        this.textPattern = textPattern;
        this.cmd = cmd;
    }

    @Override
    public TEXT_PATTERN textPattern() {
        return textPattern;
    }

    @Override
    public CMD cmd() {
        return cmd;
    }

    @Override
    public String friendlyString() {
        ArrayList<String> out = new ArrayList<>();
        if(textPattern != null){ out.add("textPattern: " + textPattern.toString()); }
        if(cmd != null){ out.add("cmd: " + cmd.toString()); }
        return String.join(", ", out);
    }
}
