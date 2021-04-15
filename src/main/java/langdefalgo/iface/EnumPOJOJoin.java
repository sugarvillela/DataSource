package langdefalgo.iface;

import langdefalgo.impl.AlgoBase;

public interface EnumPOJOJoin {
    void initAlgo(AlgoBase childAlgo, IAlgoStrategy[] pushes, IAlgoStrategy[] pops);
    void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo, IAlgoStrategy[] pushes, IAlgoStrategy[] pops);
    LANG_STRUCT getParentEnum();
    LANG_STRUCT getChildAlgo();
}
