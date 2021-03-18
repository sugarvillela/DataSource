package datasource.dec;

import datasource.iface.IDataSource;
import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import runstate.Glob;
import textevent.iface.ITextEventFactory;
import textevent.iface.ITextEventNode;
import textevent.iface.ITextEventTemplate;
import textevent.impl.TextEventNode;
import textevent.impl.TextEventTemplate;

import java.util.Map;

import static langdef.CMD.PUSH;
import static langdef.STRUCT_KEYWORD.RX;

/** Matches text with enums defined in LANG_STRUCT.
 *  If exact match, node.textEvent is updated with the enum so later parsing can respond to it.
 *
 *  Current language definition:
 *    If a name is to be assigned to a text pattern, the name immediately follows the structure keyword.
 *    Naming done by the idDefine text pattern.
 *    If name in idDefine event is copied to the structure keyword event, the idDefine event is
 *    marked inactive and removed by SourceActiveOnly.
 *
 *  Current language definition:
 *    Certain lang struct are copy-only, meaning any actions related to nesting should be disabled.
 *    Set nesting rule to copyOnly.
 *      The copy-only lang struct must have an exclusive pop symbol, meaning that the only way to pop
 *      it is with the symbol (guarantees the symbol will be found in the front-end language use)
 *  */
public class SourceTextPattern extends DecoratorBase{
    private final Map<String, TextEventTemplate> map;
    private final LANG_STRUCT idDefine;
    private IReadNode currNode, nextNode;
    ITextEventNode textEvent;
    private LANG_STRUCT stackTop;

    public SourceTextPattern(IDataSource dataSource) {
        super(dataSource);
        map = Glob.ENUMS_BY_TYPE.langStructEventMapForPatternMatch();
        idDefine = Glob.ENUMS_BY_TYPE.sourceTextPatternIdDefine();
        next();
    }

    @Override
    public boolean hasNext() {
        return nextNode != null;
    }

    /*=====structure find=============================================================================================*/

    private boolean nextNodeMatch(){
        if(nextNode != null){
            ITextEventTemplate textEventTemplate;

            String text = nextNode.text();

            if((textEventTemplate = map.get(text)) != null){
                textEvent = new TextEventNode(
                        textEventTemplate.langStruct(), textEventTemplate.cmd()
                );
            }
            else if((textEventTemplate = map.get(text.substring(0, 1))) != null && textEventTemplate.useSubstring()){
                textEvent = new TextEventNode(
                        textEventTemplate.langStruct(), textEventTemplate.cmd(), text.substring(1)
                );
            }
        }
        return (textEvent != null);
    }

    /*=====mute=======================================================================================================*/

    private boolean shouldAddTextEvent(){
        if(isMuted()){// if unMuting, should add the current pop event
            return (shouldUnMute() && unMute());
        }
        else if(shouldMute()){// if muting, should add the current push event
            mute();
        }
        return true;
    }
    private void addTextEvent(){
        nextNode.setTextEvent(textEvent);
    }

    private boolean shouldMute(){
        return textEvent.langStruct().getNestingRule().isCopyOnly();
    }
    private boolean mute(){
        stackTop = textEvent.langStruct();
        return true;
    }

    private boolean shouldUnMute(){
        return textEvent.cmd() == CMD.POP && textEvent.langStruct() == stackTop;
    }
    private boolean unMute(){
        stackTop = null;
        return true;
    }
    private boolean isMuted(){
        return stackTop != null;
    }
    /*=====structure naming===========================================================================================*/

    private boolean shouldCopySubstring(){
        return textEvent != null &&
                textEvent.langStruct() == idDefine &&
            currNode != null && currNode.hasTextEvent() &&
            currNode.textEvent().cmd() == PUSH && currNode.active();
    }

    private void copySubstring(){
        currNode.textEvent().setSubstring(
                textEvent.substring()
        );
        nextNode.setActive(false);
    }

    @Override
    public IReadNode next() {
        currNode = nextNode;
        nextNode = dataSource.next();
        textEvent = null;
        if(nextNodeMatch()){
            if(shouldAddTextEvent()){
                addTextEvent();
            }
            if(shouldCopySubstring()){
                copySubstring();
            }
        }
        return currNode;
    }
}
