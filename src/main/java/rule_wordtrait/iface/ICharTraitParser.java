package rule_wordtrait.iface;

public interface ICharTraitParser {
    ICharTraitParser setText(String text);

    ICharTraitParser parse();

    boolean foundTrait();

    String getWatchedTraits();

    String getFoundTraits();

    interface IBuilder {

        IBuilder traits(ICharTrait... traits);

        IBuilder skipSymbols(String openingSymbols);

        IBuilder skipSymbols(char oneOpeningSymbol);

        IBuilder skipSymbols(char openingSymbol, char closingSymbol);

        IBuilder skipSymbols(char[] oMap, char[] cMap);

        IBuilder keepEscapeSymbol();

        ICharTraitParser build();
    }
}
