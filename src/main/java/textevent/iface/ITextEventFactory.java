package textevent.iface;

import langdef.CMD;

public interface ITextEventFactory {
    ITextEventNode newTextEvent();
    ITextEventNode newTextEvent(String substring);

    boolean useSubstring();
}
