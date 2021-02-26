package visitor.impl;

import datasource.LocalStatics;
import readnode.iface.IReadNode;
import textpattern.TEXT_PATTERN;
import visitor.iface.IEventProvider;
import visitor.iface.IEventReceiver;

import static datasource.LocalStatics.FACTORY_DATA_SOURCE;

/** Some DataSource decorators keep internal state out of sync with the eventual output.
 *  SourceFluid needs immediate triggering to push a new file on time
 *  IEventProvider can be passed as a trigger event to tell SourceFluid to push a file.
 *  SourceFluid changes state on a call to its receive() method.
 *
 *  Implemented here for clarity, but an anonymous impl would suffice (with values hard-coded).
 *  This impl demonstrates loading a second file from a keyword in the first file */
public class EventProviderFluid implements IEventProvider {
    private final TEXT_PATTERN[] textPatterns;
    private boolean state;

    public EventProviderFluid(TEXT_PATTERN... textPatterns) {
        this.textPatterns = textPatterns;
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
                        LocalStatics.FILE_NAME_UTIL.mergeDefaultPath(requestData.text()), null
                    )
                );
                state = false;
            }
            else if(requestData.textPattern() != null && findInTextPatterns(requestData.textPattern())){
                requestData.setActive(false);
                state = true;
            }
        }
    }
    private boolean findInTextPatterns(TEXT_PATTERN textPattern){
        for(int i = 0; i < textPatterns.length; i++){
            if(textPatterns[i] == textPattern){
                return true;
            }
        }
        return false;
    }
}
