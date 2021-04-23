package rule_wordtrait.iface;

import langdefsub.PAR_TYPE;

/** A visitor pattern for clients of WordTrait utilities to extract content from found pattern */
public interface IWordTraitClient {
    void receiveContent(String... content);
    void receiveContent(int... content);
    void receiveContent(PAR_TYPE content);
}
