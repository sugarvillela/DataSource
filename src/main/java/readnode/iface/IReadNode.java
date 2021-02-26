package readnode.iface;

import textpattern.TEXT_PATTERN;

/** See accompanying javadoc in IDataSource.
 *  Contextual comments below in code */
public interface IReadNode extends Comparable<IReadNode> {

    void setActive(boolean active);     // Set false if node should be ignored by downstream parsing
    void setHasNext(boolean hasNext);   // Overridable done state (used by SourceFluid to continue on source pop)
    void setTextPattern(                // Set a TEXT_PATTERN enum if matching text found (set by PatternMatch)
            TEXT_PATTERN textPattern
    );

    // immutable status
    String source();                    // Short fileName or unique id for array, list etc.
    int row();                          // Line number, zero index
    int col();                          // Row mode: always zero; Tokenize mode: word number, zero index

    // immutable data
    String text();                      // Row mode: a line of text; Tokenize mode: a word
    int indent();                       // Tokenize mode: the number of spaces removed from the front when trimming
    boolean endLine();                  // Row mode: always true; Tokenize mode: true on last word

    // state
    boolean active();                   // Default true
    boolean hasNext();                  // Row mode: false on last row; Tokenize mode: false on all words in last row (overridden by SourceFluid to continue on source pop)
    boolean hasPattern();               // Default false unless textPattern is set
    TEXT_PATTERN textPattern();         // Null unless text matches a TEXT_PATTERN enum (set by PatternMatch)

    // to string
    String indentedText();              // Row mode: same as text(); Tokenize mode: recreate indented text (back spaces still discarded)
    String statusString();              // CSV of source, row, col
    String friendlyStatusString();      // Human-friendly source, row, col: test.txt line 7 row 6
    String friendlyString();            // All node data except for nulls
    String csvString();                 // CSV of all data

    int sortValue();                    // Values assigned sequentially on node create, for sorting by creation order
}
