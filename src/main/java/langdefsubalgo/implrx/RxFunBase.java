package langdefsubalgo.implrx;

import langdefsub.FUN_TYPE;
import langdefsub.PAR_TYPE;
import langdefsub.PRIM_TYPE;
import langdefsubalgo.iface.IFun;

import java.util.Arrays;

public abstract class RxFunBase implements IFun {
    protected final FUN_TYPE funType;
    protected final PRIM_TYPE inType, outType;
    protected final PAR_TYPE[] paramTypes;

    protected RxFunBase(FUN_TYPE funType, PRIM_TYPE inType, PRIM_TYPE outType, PAR_TYPE... paramTypes) {
        this.funType = funType;
        this.paramTypes = paramTypes;
        this.inType = inType;
        this.outType = outType;
    }

    @Override
    public String description() {
        return String.format("%s -> %s() -> %s, params %s",
            inType, funType, outType, Arrays.toString(paramTypes)
        );
    }

    @Override
    public FUN_TYPE funType() {
        return funType;
    }

    @Override
    public PRIM_TYPE inType() {
        return inType;
    }

    @Override
    public PRIM_TYPE outType() {
        return outType;
    }

    @Override
    public PAR_TYPE[] paramTypes() {
        return paramTypes;
    }
}
