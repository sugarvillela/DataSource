package langdefsubalgo.implfx;

import langdefsub.PAR_TYPE;
import langdefsub.PRIM_TYPE;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunList;

import java.util.ArrayList;
import java.util.List;

public class FxAccessListWrap  implements IFunList {
    private final List<IFun> accessList;

    public FxAccessListWrap(String text, boolean accessMod) {
        accessList = new ArrayList<>(1);
        accessList.add(new FxAccess(null, text, accessMod));
    }

    @Override
    public PRIM_TYPE outType() {
        return null;
    }

    @Override
    public PAR_TYPE[] accessParamTypes() {
        return accessList.get(0).parTypesBefore();
    }

    @Override
    public List<IFun> toList() {
        return accessList;
    }

    @Override
    public String toString() {
        return accessList.get(0).toString();
    }
}
