package langdefsubalgo.impl;

import langdefsub.FUN_TYPE;
import langdefsub.PAR_TYPE;
import langdefsub.PRIM_TYPE;
import langdefsubalgo.iface.IFun;

import java.util.Arrays;

public abstract class FunBase implements IFun {
    // actual local attributes
    protected FUN_TYPE funType;
    protected PAR_TYPE parTypeEnum;
    protected final IFun prev;

    // allowed surrounding attributes
    protected PRIM_TYPE primTypeBefore, primTypeAfter;
    protected PAR_TYPE[] paramTypes;
    protected PAR_TYPE[] parTypesBefore;
    protected FUN_TYPE[] funTypesBefore;

    protected FunBase(IFun prev){
        this.prev = prev;
    }

    protected void setFunType(FUN_TYPE funType){
        this.funType = funType;
    }
    protected void setParamTypes(PAR_TYPE... paramTypes){
        this.paramTypes = paramTypes;
    }

    protected void setPrimTypeBefore(PRIM_TYPE primTypeBefore){
        this.primTypeBefore = primTypeBefore;
    }
    protected void setPrimTypeAfter(PRIM_TYPE primTypeAfter){
        this.primTypeAfter = primTypeAfter;
    }

    protected void setParTypesBefore(PAR_TYPE... parTypesBefore){
        this.parTypesBefore = parTypesBefore;
    }
    protected void setFunTypesBefore(FUN_TYPE... funTypesBefore){
        this.funTypesBefore = funTypesBefore;
    }

    /*=====IFun methods===============================================================================================*/

    @Override
    public String description() {
        return String.format("%s -> %s() -> %s, params: %s",
                primTypeBefore, funType, primTypeAfter, Arrays.toString(paramTypes)
        );
    }

    @Override
    public FUN_TYPE funType() {
        return funType;
    }

    @Override
    public PAR_TYPE[] allowedParamTypes() {
        return paramTypes;
    }


    @Override
    public PRIM_TYPE primTypeBefore() {
        return primTypeBefore;
    }

    @Override
    public PRIM_TYPE primTypeAfter() {
        return primTypeAfter;
    }

    @Override
    public FUN_TYPE[] funTypesBefore() {
        return funTypesBefore;
    }

    @Override
    public PAR_TYPE[] parTypesBefore() {
        return parTypesBefore;
    }

    /*=====IWordTraitClient methods===================================================================================*/

    @Override
    public void receiveContent(String... content) {}

    @Override
    public void receiveContent(int... content) {}

    @Override
    public void receiveContent(PAR_TYPE content) {
        this.parTypeEnum = content;
    }

    @Override
    public String[] stringContent() {
        return null;
    }

    @Override
    public int[] intContent() {
        return null;
    }

    @Override
    public PAR_TYPE parType() {
        return parTypeEnum;
    }

    /*=====Object method==============================================================================================*/

    @Override
    public String toString() {

        return "FunBase{" +
                "funType=" + funType +
                ", parTypesBefore=" + Arrays.toString(parTypesBefore) +
                ", funTypesBefore=" + Arrays.toString(funTypesBefore) +
                ", parTypeEnum=" + parTypeEnum +
                "\n" + this.description() +
                "\n}";
    }
}
