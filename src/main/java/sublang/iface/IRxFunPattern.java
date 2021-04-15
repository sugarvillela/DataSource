package sublang.iface;

import sublang.COMPARE;

public interface IRxFunPattern {
    IRxFun[] left();
    COMPARE compare();
    IRxFun[] right();
}
