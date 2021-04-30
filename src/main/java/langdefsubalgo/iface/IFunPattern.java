package langdefsubalgo.iface;

import langdefsub.COMPARE;

import java.util.List;

/** IRxFunList = IRxFunList */
public interface IFunPattern {
    List<IFun> left();
    COMPARE compare();
    List<IFun> right();
}
