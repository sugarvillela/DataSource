package wordtraitutil.iface;

import err.ERR_TYPE;

public interface IWordTraitAction {
    ERR_TYPE doAction(IWordTraitClient client, String text);
}
