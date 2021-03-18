package datasource.iface;

import readnode.iface.IReadNode;

/** Needed: a universal iterator to work on all sources interchangeably:
 *      Arrays, Lists, small files (from memory), large files (from disk), strings (tokenized on space).
 *
 *  Implementations should provide:
 *      Empty line skipping.
 *      Word-by-word iteration of source (tokenizing to a continuous word stream).
 *      Peek forward and peek goBack.
 *      Source switching on-the-fly, meaning you can push a new source, finish it and resume the previous source.
 *      Recognition of keywords in text to trigger source switching
 *
 *  This is best accomplished using a decorator pattern, with each decorator adding one of the above functionalities.
 *  Mix and match by wrapping decorators at the class constructor.
 *  In the code, distinguish between 'core' implementations, which iterate through the source,
 *  and 'decorator' implementations, which manipulate the data coming from the core implementations.
 *
 *  Each iterator output should provide accurate status with:
 *      current source name:        file name or list description
 *      row:                        line in file, element in string array etc.
 *      column:                     word in line
 *      indent:                     the number of spaces removed from front while trimming
 *      isEndLine:                  meaningful only in word-tokenize mode
 *      hasNext:                    true except for the last item
 *      active:                     if false, the node can be ignored in parsing
 *      the current string token:   a row or a word, depending on word-tokenize mode
 *      textEvent:                an enum that is found to match a word of text
 *
 *  This is best accomplished by returning an IReadNode object instead of a string token.
 *  Each IReadNode has its own status fields, correct no matter what order things are called in.
 *  Each IReadNode is stamped with a sequential id on create so they are sortable(IReadNode extends java Comparable).
 *  The ability to sort read nodes is useful in cases where the original order is lost and needs to be recreated
 *  When used in a parser, errors can be tracked to file, line and word.
 *
 *  Additionally, text event notifiers are needed to assure on-time state changes (see visitor.impl.EventProviderFluid).
 *  This is best accomplished using a visitor pattern with IEventProvider and IEventReceiver objects.
 *  Decorators that need to change state based on text events should implement IEventReceiver.
 *  Implementing IEventProvider adds custom behavior to these classes:
 *      SourceFluid pushes additional source as provided by an IEventProvider.
 *
 *  No source should break on overrun; should return null indefinitely if caller ignores false hasNext()
 *
 *  Notes:
 *  The availability of status data from each node simplifies the interface.
 *      You could remove everything except hasData() and next() and still have a functioning iterator by
 *      keeping track of node status.
 *  No 'rewind' or 'reset' is provided. Objects should be single purpose; resetting adds complexity
 *  Each component is unit tested; see 'test' directory
 *  FactoryDataSource provides a ready-made decorator configuration that passes tests.
 */
public interface IDataSource {
    String sourceName();    // short file name or unique array/list identifier
    boolean hasData();      // check once on start, or fold into hasNext()

    boolean hasNext();      // false on no more items
    boolean hasPeekBack();  // false on first item
    boolean hasPeekAhead(); // false on last item

    IReadNode next();       // current item,    then inc internal pointer
    IReadNode peekBack();   // previous item,   no inc
    IReadNode peekAhead();  // next item,       no inc

}
