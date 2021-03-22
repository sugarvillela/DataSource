package stack.impl;

import langdefalgo.iface.LANG_STRUCT;
import stack.iface.IStackLogIterationItem;

public class StackLogIterationItem implements IStackLogIterationItem {
    private final LANG_STRUCT langStruct;

    public StackLogIterationItem(LANG_STRUCT langStruct) {
        this.langStruct = langStruct;
    }

    @Override
    public LANG_STRUCT langStruct() {
        return langStruct;
    }

    @Override
    public String csvString() {
        return langStruct.toString();
    }
}
