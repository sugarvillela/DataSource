package rule_wordtrait.impl;

import langdefsub.PAR_TYPE;
import rule_wordtrait.iface.IWordTraitAction;
import rule_wordtrait.iface.IWordTraitClient;
import rule_wordtrait.iface.ICharTraitParser;
import rule_wordtrait.iface.ITraitPatternMatch;

import static langdefsub.PAR_TYPE.EMPTY_PAR;
import static rule_wordtrait.impl.WordTraitActionImplGroup.ActionEmptyPar;

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
        if(text.isEmpty()){
            return ActionEmptyPar.initInstance().doAction(client, EMPTY_PAR, null);
        }
        String traitText = charTraitParser.setText(text).parse().getFoundTraits();
        //System.out.printf("TraitPatternMatch: for text %s traitText = %s \n", text, traitText);
        return TraitPatternUtil.initInstance().match(traitText, pattern) &&
                action.doAction(client, parType, text);
    }
}
