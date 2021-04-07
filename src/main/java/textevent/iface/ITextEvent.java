package textevent.iface;

import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;

/** Immutable data object */
public interface ITextEvent {
    LANG_STRUCT langStruct();
    CMD cmd();

    void setSubstring(String substring);
    boolean hasSubstring();
    String substring();

    String csvString();
    String friendlyString();
}
