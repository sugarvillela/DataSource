package textevent.iface;

import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;

/** A tuple with attributes for creation of new text events */
public interface ITextEventTemplate {
    LANG_STRUCT langStruct();
    CMD cmd();
    boolean useSubstring();
}
