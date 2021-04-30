package langdefsubalgo.factory;

import langdefsubalgo.iface.IPatternFactory;
import langdefsubalgo.iface.IFunPattern;
import langdefsubalgo.implrx.RxFunPattern;

public class FactoryRx implements IPatternFactory {
    private static FactoryRx instance;

    public static FactoryRx initInstance(){
        return (instance == null)? (instance = new FactoryRx()): instance;
    }

    private FactoryRx(){}

    @Override
    public IFunPattern newFunPattern(String text) {
        return new RxFunPattern(text);
    }

}
