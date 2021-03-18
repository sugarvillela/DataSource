package datasource.dec;

import datasource.iface.IDataSource;
import langdefalgo.iface.LANG_STRUCT;
import runstate.Glob;

public class SourceNonComment extends InterceptorBase{
    private final LANG_STRUCT langStruct;
    private boolean skipping;
    // inherited: IReadNode getCurrNode, nextNode;

    public SourceNonComment(IDataSource dataSource) {
        super(dataSource);
        this.langStruct = Glob.ENUMS_BY_TYPE.sourceNonCommentLangStruct();// keep all hard-code langDef in lang def package
    }

    // Tough logic, so pay attention:
    @Override
    protected boolean shouldSkip() {
        if(skipping){                           // handle subsequent skipping state
            if(nextNode.endLine()){             // comment ends at endLine
                skipping = false;               // skip this one, but goBack to normal next time
            }
            return true;
        }
        else if(                                // kill initial skipping state
                nextNode.textEvent() != null &&
                this.langStruct == nextNode.textEvent().langStruct()
        ){
            skipping = !nextNode.endLine();     // if one-word comment, only skip this one
            if(currNode != null && !currNode.endLine()){            // getCurrNode refers to the previous word
                currNode.setEndLine(true);      // endLine will be false unless getCurrNode is end of prev line
            }
            return true;
        }
        return false;
    }
}
