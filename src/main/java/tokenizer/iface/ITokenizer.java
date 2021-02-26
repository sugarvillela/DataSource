package tokenizer.iface;

import java.util.ArrayList;

public interface ITokenizer {
    ITokenizer parse(String text);
    ArrayList<String> getArrayList();
    String[] getArray();

    /** Builder interface used to build the more complex implementation of Tokenizer */
    interface Builder{
        /**Supports multiple delimiters
         * @param delimiters All delimiters, for example: " _-"
         */
        Builder delimiters(String delimiters);

        /**Supports multiple delimiters
         * @param oneDelimiter Can pass single char, for example ' '
         */
        Builder delimiters(char oneDelimiter);

        /**Areas enclosed in symbols are skipped by the tokenizer
         * Supports '(','{','[','<', single- and double-quote
         * Automatically adds the appropriate closing symbols
         * @param openingSymbols All opening symbols, for example "({'"
         */
        Builder skipSymbols(String openingSymbols);

        Builder skipSymbols(char openingSymbol);

        /**To use symbols not already provided, pass your own
         * @param oMap opening symbols
         * @param cMap closing symbols, must match oMap index and size
         */
        Builder skipSymbols(char[] oMap, char[]cMap);

        /**Tokenizer removes outermost skip symbols by default
         * Setting keepSkipSymbol leaves the symbols in */
        Builder keepSkipSymbol();

        /**Tokenizer discards delimiters by default
         * Setting delimiterToElement causes delimiter to be written to
         * its own element (repeated delimiters are not ignored) */
        Builder tokenizeDelimiter();

        /**Same as delimiterToElement() except repeated delimiters are ignored */
        Builder tokenizeDelimiterOnce();

        ITokenizer build();
    }
}
