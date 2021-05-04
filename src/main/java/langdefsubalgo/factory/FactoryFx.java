package langdefsubalgo.factory;

import langdefsubalgo.iface.IFunBuildUtil;
import langdefsubalgo.iface.IFunPattern;
import langdefsubalgo.iface.IPatternFactory;
import langdefsubalgo.implfx.FxFunPattern;

public class FactoryFx  implements IPatternFactory {
    private static FactoryFx instance;

    public static FactoryFx initInstance(){
        return (instance == null)? (instance = new FactoryFx()): instance;
    }

    private FactoryFx(){}

    @Override
    public IFunPattern newFunPattern(String text) {
        return new FxFunPattern(text);
    }

    @Override
    public IFunBuildUtil getFunBuildUtil() {
        return FunBuildUtilFx.initInstance();
    }
}
