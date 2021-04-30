package rule_wordtrait.impl;

import langdefsub.PAR_TYPE;
import rule_wordtrait.iface.IWordTraitClient;

public abstract class WordTraitClientBase implements IWordTraitClient {
    protected String[] strings;
    protected int[] numbers;
    protected PAR_TYPE parTypeEnum;

    @Override
    public void receiveContent(String... content) {
        strings = content;
        numbers = null;
    }

    @Override
    public void receiveContent(int... content) {
        numbers = content;
        strings = null;
    }

    @Override
    public void receiveContent(PAR_TYPE content) {
        parTypeEnum = content;
    }
}
