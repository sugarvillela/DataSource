package rule_wordtrait.iface;

public interface ITraitPatternMatch {
    boolean tryParse(IWordTraitClient client, String text);
}
