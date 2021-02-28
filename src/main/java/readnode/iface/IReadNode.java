package readnode.iface;

import langdef.CMD;
import textevent.iface.ITextEventNode;

/** See accompanying javadoc in IDataSource.
 *  Contextual comments below in code */
public interface IReadNode extends Comparable<IReadNode> {
    void setText(String text);          // Overridable (PatternMatch sets a substring)
    void setActive(boolean active);     // Set false if node should be ignored by downstream parsing
    void setEndLine(boolean endLine);   // Overridable (SourceNonComment sets true)
    void setHasNext(boolean hasNext);   // Overridable done state (used by SourceFluid to continue on source pop)
    void setLangStruct(ITextEventNode textEventNode); //  (set by PatternMatch)

    // immutable status
    String source();                    // Short fileName or unique id for array, list etc.
    int row();                          // Line number, zero index
    int col();                          // Row mode: always zero; Tokenize mode: word number, zero index

    // immutable data
    int indent();                       // Tokenize mode: the number of spaces removed from the front when trimming


    // state
    boolean active();                   // Default true
    boolean endLine();                  // Row mode: always true; Tokenize mode: true on last word
    boolean hasNext();                  // Row mode: false on last row; Tokenize mode: false on all words in last row (overridden by SourceFluid to continue on source pop)
    boolean hasTextEvent();             // Default false unless textEvent is set
    ITextEventNode textEvent();         // Null unless text matches a TEXT_PATTERN enum (set by PatternMatch)

    // to string
    String text();                      // Row mode: a line of text; Tokenize mode: a word
    String indentedText();              // Row mode: same as text(); Tokenize mode: recreate indented text (back spaces still discarded)
    String statusString();              // CSV of source, row, col
    String friendlyStatusString();      // Human-friendly source, row, col: test.txt line 7 row 6
    String friendlyString();            // All node data except for nulls
    String csvString();                 // CSV of all data

    int sortValue();                    // Values assigned sequentially on node create, for sorting by creation order

    CMD cmd();
    String key();
}
