package visitor.impl;

import runstate.Glob;
import langdef.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import visitor.iface.IEventProvider;
import visitor.iface.IEventReceiver;

import static runstate.Glob.FACTORY_DATA_SOURCE;

/** Some DataSource decorators keep internal state out of sync with the eventual output.
 *  SourceFluid needs immediate triggering to push a new file on time
 *  IEventProvider can be passed as a trigger event to tell SourceFluid to push a file.
 *  SourceFluid changes state on a call to its receive() method.
 *
 *  Implemented here for clarity, but an anonymous impl would suffice (with values hard-coded).
 *  This impl demonstrates loading a second file from a keyword in the first file */
public class EventProviderFluid implements IEventProvider {
    private final LANG_STRUCT[] langStructs;
    private boolean state;

    public EventProviderFluid(LANG_STRUCT... langStructs) {
        this.langStructs = langStructs;
        this.state = false;
    }

    @Override
    public void provide(IEventReceiver receiver) {}

    @Override
    public void provide(IEventReceiver receiver, IReadNode requestData) {
        if(requestData != null){
            if(state ){
                requestData.setActive(false);
                receiver.receive(this,FACTORY_DATA_SOURCE.getSourceTok(
                        Glob.FILE_NAME_UTIL.mergeDefaultPath(requestData.text()), null
                    )
                );
                state = false;
            }
            else if(requestData.langStruct() != null && isActionableLangStruct(requestData.langStruct())){
                requestData.setActive(false);
                state = true;
            }
        }
    }
    private boolean isActionableLangStruct(LANG_STRUCT langStruct){
        for(int i = 0; i < langStructs.length; i++){
            if(langStructs[i] == langStruct){
                return true;
            }
        }
        return false;
    }
}
