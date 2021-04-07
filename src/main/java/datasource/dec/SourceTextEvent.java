package datasource.dec;

import datasource.iface.IDataSource;
import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import runstate.Glob;
import textevent.iface.ITextEvent;
import textevent.iface.ITextEventTemplate;
import textevent.impl.TextEvent;
import textevent.impl.TextEventTemplate;

import java.util.Map;

import static langdef.CMD.PUSH;

/** Matches text with enums defined in LANG_STRUCT.
 *  If exact match, node.textEvent is updated with the enum so later parsing can respond to it.
 *
 *  Current language definition:
 *    If a name is to be assigned to a text pattern, the name immediately follows the structure keyword.
 *    Naming done by the idDefine text pattern.
 *    If name in idDefine event is copied to the structure keyword event, the idDefine event is
 *    marked inactive and removed by SourceActiveOnly.
 *  */
public class SourceTextEvent extends DecoratorBase{
    private final Map<String, TextEventTemplate> map;
    private final ListenUtil listenUtil;
    private final IdentifierUtil identifierUtil;
    private final LANG_STRUCT idDefine;
    private IReadNode prevNode, currNode;
    private ITextEvent textEvent;

    public SourceTextEvent(IDataSource dataSource) {
        super(dataSource);
        map = Glob.ENUMS_BY_TYPE.langStructEventMapForPatternMatch();
        listenUtil = new ListenUtil();
        identifierUtil = new IdentifierUtil();

        idDefine = Glob.ENUMS_BY_TYPE.enumIdDefine();
        next();
    }

    @Override
    public boolean hasNext() {
        return currNode != null;
    }

    /*=====structure find=============================================================================================*/

    private boolean findTextEvent(){
        ITextEventTemplate textEventTemplate;
        String text = currNode.text();

        textEvent = null;
        if((textEventTemplate = map.get(text)) != null){
            textEvent = new TextEvent(
                    textEventTemplate.langStruct(), textEventTemplate.cmd()
            );
        }
        else if((textEventTemplate = map.get(text.substring(0, 1))) != null && textEventTemplate.useSubstring()){
            textEvent = new TextEvent(
                    textEventTemplate.langStruct(), textEventTemplate.cmd(), text.substring(1)
            );
        }
        return textEvent != null;
    }

    @Override
    public IReadNode next() {
        prevNode = currNode;
        currNode = dataSource.next();
        if(currNode != null){

            if(
                    this.findTextEvent() &&
                    listenUtil.tryAddTextEvent(textEvent, currNode) &&
                            identifierUtil.tryCopySubstring(textEvent, prevNode, currNode)
            ){
                if(listenUtil.tryAddTextEvent(textEvent, currNode)){
                    currNode.setTextEvent(textEvent);
                    identifierUtil.tryCopySubstring(textEvent, prevNode, currNode);
//                    if(shouldCopySubstring()){
//                        if(prevNode.textEvent().langStruct() == Glob.ENUMS_BY_TYPE.enumCodeBlock()){
//                            this.setCodeBlockIdentifierError();
//                        }
//                        else{
//                            copySubstring();
//                        }
//                    }
                }

            }
        }
        return prevNode;
    }

    public static class ListenUtil {
        private boolean L, P;   // Listen now, listen previous

        public ListenUtil() {
            L = Glob.INITIAL_LISTEN_STATE;
        }

        public boolean tryAddTextEvent(ITextEvent textEvent, IReadNode currNode){
            if(this.shouldAddTextEvent(textEvent)){
                currNode.setTextEvent(textEvent);
                return true;
            }
            return false;
        }

        public boolean shouldAddTextEvent(ITextEvent textEvent){
            LANG_STRUCT langStruct = textEvent.langStruct();
            CMD cmd = textEvent.cmd();

            P = L;
            if(this.shouldChangeState(langStruct, cmd)){
                return this.shouldChangeNow(langStruct , cmd )? L : P;
            }
            return P;
        }

        private boolean shouldChangeState(LANG_STRUCT langStruct, CMD cmd){
            // Listen state change: L = T or F
            // T && T_PUSH ->     F no change
            // S && S_PUSH ->     T invalid
            // T && T_POP  ->     T
            // T && S_PUSH ->     T
            // S && S_POP  ->     F
            // S && T_PUSH  ->    F
            if(langStruct == Glob.ENUMS_BY_TYPE.enumLangT()){
                switch (cmd){
                    case PUSH:
                        L = false;
                        return true;
                    case POP:
                        L = true;
                        return true;
                }
            }
            if(langStruct == Glob.ENUMS_BY_TYPE.enumLangS()){
                switch (cmd){
                    case POP:
                        L = false;
                        return true;
                    case PUSH:
                        L = true;
                        return true;
                }
            }
            return false;
        }

        private boolean shouldChangeNow(LANG_STRUCT langStruct, CMD cmd){
            // !P && L -> T popped or S pushed
            //      T popped    now
            //      S pushed    now
            // P && !L -> S popped or T pushed
            //      S popped    later
            //      T pushed    later

            return !P && L;
        }
    }

    public static class IdentifierUtil {
        private final LANG_STRUCT idDefine;

        public IdentifierUtil() {
            idDefine = Glob.ENUMS_BY_TYPE.enumIdDefine();
        }

        private boolean tryCopySubstring(ITextEvent textEvent, IReadNode prevNode, IReadNode currNode){
            if(shouldCopyIdentifier(textEvent, prevNode)){
                prevNode.textEvent().setSubstring(
                        textEvent.substring()
                );
                currNode.setActive(false);
                return true;
            }
            return false;
        }
        private boolean shouldCopyIdentifier(ITextEvent textEvent, IReadNode prevNode){
            return textEvent.langStruct() == idDefine &&
                    prevNode != null && prevNode.hasTextEvent() &&
                    prevNode.textEvent().cmd() == PUSH && prevNode.active();
        }

        private void setCodeBlockIdentifierError(ITextEvent textEvent, IReadNode prevNode, IReadNode currNode){
            if(prevNode.textEvent().langStruct() == Glob.ENUMS_BY_TYPE.enumCodeBlock()){
                LANG_STRUCT codeBlockEnum = Glob.ENUMS_BY_TYPE.enumCodeBlock();
                String message = String.format(
                        "Expected identifier '%s' before %s operator",
                        textEvent.substring(), codeBlockEnum.getPushSymbol()
                );
                Glob.ERR.kill(currNode, message);
            }
        }
    }
}
