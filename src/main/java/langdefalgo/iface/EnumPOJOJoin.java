package langdefalgo.iface;

import langdefalgo.impl.AlgoBase;

public interface EnumPOJOJoin {
    void initAlgo(AlgoBase childAlgo);
    void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo);
    LANG_STRUCT getParentEnum();
    LANG_STRUCT getChildAlgo();
}
