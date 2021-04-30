package langdefsubalgo.implfx;

import langdefsub.PRIM_TYPE;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunList;

import java.util.List;

public class FxAccess implements IFunList {
    private final boolean accessMod;

    public FxAccess(String text, boolean accessMod) {
        this.accessMod = accessMod;
    }

    @Override
    public PRIM_TYPE outType() {
        return null;
    }

    @Override
    public List<IFun> toList() {
        return null;
    }
}
