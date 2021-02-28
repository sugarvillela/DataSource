package textevent.iface;

import langdef.CMD;
import langdef.iface.TEXT_PATTERN;

/** Immutable data object */
public interface ITextEventNode {
    TEXT_PATTERN textPattern();
    CMD cmd();

    String friendlyString();
}
