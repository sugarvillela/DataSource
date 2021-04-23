package rule_wordtrait.impl;

import langdefsub.PAR_TYPE;
import rule_wordtrait.iface.IWordTraitAction;
import rule_wordtrait.iface.IWordTraitClient;
import rule_wordtrait.iface.ICharTraitParser;
import rule_wordtrait.iface.ITraitPatternMatch;

public class TraitPatternMatch implements ITraitPatternMatch {
    private final ICharTraitParser charTraitParser;
    private final String pattern;
    private final IWordTraitAction action;
    private final PAR_TYPE parType;

    public TraitPatternMatch(ICharTraitParser charTraitParser, String pattern, PAR_TYPE parType, IWordTraitAction action) {
        this.charTraitParser = charTraitParser;
        this.pattern = TraitPatternUtil.initInstance().sortPattern(pattern);
        this.parType = parType;
        this.action = action;
    }

    @Override
    public boolean tryParse(IWordTraitClient client, String text) {
        String traitText = charTraitParser.setText(text).parse().getFoundTraits();

        if(TraitPatternUtil.initInstance().match(traitText, pattern)){
            client.receiveContent(parType);
            action.doAction(client, text);
            return true;
        }
        return false;
    }
}
