package langdefsubalgo.iface;

import langdefsub.FUN_TYPE;
import langdefsub.PAR_TYPE;
import langdefsub.PRIM_TYPE;

public interface IFun {
    String description();
    FUN_TYPE funType();
    PRIM_TYPE inType();
    PRIM_TYPE outType();
    PAR_TYPE[] paramTypes();
}
