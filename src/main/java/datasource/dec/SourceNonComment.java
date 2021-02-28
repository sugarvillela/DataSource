package datasource.dec;

import datasource.iface.IDataSource;
import langdef.iface.TEXT_PATTERN;
import runstate.Glob;

public class SourceNonComment extends InterceptorBase{
    private final TEXT_PATTERN textPattern;
    private boolean skipping;

    public SourceNonComment(IDataSource dataSource) {
        super(dataSource);
        this.textPattern = Glob.ENUMS_BY_TYPE.sourceNonCommentTextPattern();// keep all hard-code langDef in lang def package
    }

    // Tough logic, so pay attention:
    @Override
    protected boolean shouldSkip() {
        if(skipping){                           // handle subsequent skipping state
            if(nextNode.endLine()){             // comment ends at endLine
                skipping = false;               // skip this one, but back to normal next time
            }
            return true;
        }
        else if(                                // set initial skipping state
                nextNode.textEvent() != null &&
                this.textPattern == nextNode.textEvent().textPattern()
        ){
            skipping = !nextNode.endLine();     // if one-word comment, only skip this one
            if(!currNode.endLine()){            // currNode refers to the previous word
                currNode.setEndLine(true);      // endLine will be false unless currNode is end of prev line
            }
            return true;
        }
        return false;
    }
}
