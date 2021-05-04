package langdefsubalgo.iface;

import langdefsub.PAR_TYPE;
import langdefsub.PRIM_TYPE;

import java.util.List;

/** IRxFun.IRxFun.IRxFun */
public interface IFunList {
    PRIM_TYPE outType();
    PAR_TYPE[] accessParamTypes();
    List<IFun> toList();
}
