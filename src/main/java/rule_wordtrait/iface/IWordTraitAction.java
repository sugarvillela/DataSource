package rule_wordtrait.iface;

import langdefsub.PAR_TYPE;

public interface IWordTraitAction {
    boolean doAction(IWordTraitClient client, PAR_TYPE parType, String text);
}
