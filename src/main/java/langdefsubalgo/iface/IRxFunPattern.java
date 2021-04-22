package langdefsubalgo.iface;

import langdefsub.COMPARE;

/** IRxFunList = IRxFunList */
public interface IRxFunPattern {
    void validate(); // validate left-compare-right
    IRxFun[] left();
    COMPARE compare();
    IRxFun[] right();
}
