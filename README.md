# DataSource
Iterator with additional functionality

### A general iterator to work on several sources interchangeably:
 * Arrays, Lists, small files (from memory), large files (from disk/cache), strings (tokenized on space).

### Implementations should provide additional features:
 * Empty line skipping.
 * Word-by-word iteration of source (tokenizing to a continuous word stream).
 * Peek forward and peek back.
 * Source switching on-the-fly, meaning you can push a new source, finish it and resume the previous source.
 * Recognition of keywords in text to trigger source switching

Accomplished using a decorator pattern, with each decorator adding one of the above functionalities.
 *  Mix and match by wrapping decorators at the class constructor.
 *  In the code, distinguish between 'core' implementations, which iterate through the source, and 'decorator' implementations, which manipulate the data coming from the core implementations.

### Implementations

Core:
* SourceArray, SourceList, SourceFile, SourceLargeFile, SourceCol (tokenizes a string)

Decorators:
* SourceNonEmpty:  
    *Skips empty lines in file or empty elements in array*
* SourcePeek:  
    *Adds peek forward and peek back functionality*
* SourceTok:  
    *Adds word-by-word iteration*
* PatternMatch:  
    *Locates a text pattern and sets a field so later parsing can use it*
* SourceFluid:  
    *Uses a stack to change file sources on-the-fly, returning to a previous source when the current one finishes*
    
### Each iterator output token should provide accurate status with:
 * current source name:        
     *file name or list description*
 * row:                        
     *line in file, element in string array etc.*
 * column:                     
     *word in line*
 * indent:                     
     *the number of spaces removed from front while trimming*
 * isEndLine:                  
     *meaningful only in word-tokenize mode*
 * hasNext:                    
     *true except for the last item*
 * active:                     
     *if false, the node can be ignored in parsing*
 * the current string token:   
     *a row or a word, depending on word-tokenize mode*
 * textPattern:                
     *an enum that is found to match a word of text*

Accomplished by returning an IReadNode object instead of a string token.
 * Each IReadNode has its own status fields, correct no matter what order things are called in.
 * Each IReadNode is stamped with a sequential id on create so they are sortable (IReadNode extends java Comparable).
 * The ability to sort read nodes is useful in cases where the original order is lost and needs to be recreated
 * When used in a parser, errors can be tracked to file, line and word.

### Additionally, text event notifiers are needed to assure on-time state changes.
 * Decorators may delay the output (e.g. for peekBack and peekForward operations). This takes internal state out
 of sync with text output. If you trigger a state change in the core based on some text pattern in the output, 
 the change will happen later than expected. Thus, text patterns need to be examined in the same decorator where 
 they are used.
 
 Accomplished using a visitor pattern with IEventProvider and IEventReceiver objects.
 * Decorators that need to change state based on text events should implement IEventReceiver.
 * Implementing IEventProvider adds custom behavior to class SourceFluid:
 
     SourceFluid pushes additional source as provided by an IEventProvider.

### No source should break on overrun
* Should return null indefinitely if caller ignores false hasNext()

### Notes:
 * This iterator does not implement Java Iterator, so you need to use a while loop:  
         while(dataSource.hasNext() // do stuff   
 * The availability of status data from each node simplifies the interface.
    You could remove every feature except hasData() and next() and still have a functioning iterator by keeping track of node status.
 *  No 'rewind' or 'reset' is provided. Objects should be single purpose; resetting adds complexity
 *  Each component is unit tested; see 'test' directory
 *  FactoryDataSource provides a ready-made decorator configuration that passes tests.
