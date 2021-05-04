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
    }

    @Override
    public void receiveContent(int... content) {
        numbers = content;
    }

    @Override
    public void receiveContent(PAR_TYPE content) {
        parTypeEnum = content;
    }

    @Override
    public String[] stringContent() {
        return strings;
    }

    @Override
    public int[] intContent() {
        return numbers;
    }

    public PAR_TYPE parType() {
        return parTypeEnum;
    }
}
