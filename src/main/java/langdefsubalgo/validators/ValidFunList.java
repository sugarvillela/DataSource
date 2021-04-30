package langdefsubalgo.validators;

import langdefsub.COMPARE;
import langdefsub.PAR_TYPE;
import langdefsub.PRIM_TYPE;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.iface.IFunList;
import runstate.Glob;

import java.util.ArrayList;
import java.util.Arrays;

/** Validates in-function parameter types + out-in matching between functions */
public class ValidFunList {
    private static ValidFunList instance;

    public static ValidFunList initInstance(){
        return (instance == null)? (instance = new ValidFunList()): instance;
    }

    private ValidFunList(){}

    public void validateParamType(IFun caller, PAR_TYPE actual){
        for(PAR_TYPE parType : caller.paramTypes()){
            if(parType == actual){
                return;
            }
        }
        Glob.ERR.kill(
                String.format("Wrong parameter type '%s' for sub-language function '%s'", actual, caller.funType()),
                String.format("Proper usage: %s", caller.description())
        );
    }
    public void validateRange(IFun caller, int[] numbers){
        if((numbers.length != 2)){
            Glob.ERR.kill(
                    String.format("Expected range in format RANGE(n:m) or RANGE(:m) or RANGE(n:), found %s", Arrays.toString(numbers)),
                    String.format("Proper usage: %s", caller.description())
            );
        }
        if(numbers[0] > numbers[1]){
            Glob.ERR.kill(
                    String.format("Expected range in ascending order, found %d:%d", numbers[0], numbers[1]),
                    String.format("Proper usage: %s", caller.description())
            );
        }
    }

    public void validateOutInMatch(ArrayList<IFun> funList){
        IFun prev = null;
        for(IFun curr : funList){
            PRIM_TYPE outType = (prev == null)? PRIM_TYPE.NULL : prev.outType();
            if(outType != curr.inType()){
                Glob.ERR.kill(
                    String.format("Expected input type %s for sub-language function %s, found %s",
                            curr.inType(), curr.funType(), outType
                    ),
                    String.format("Proper usage: %s", curr.description())
                );
            }
            prev = curr;
        }
    }

    public void validCompare(IFunList left, COMPARE compare, IFunList right){
        if(left.outType() != right.outType()){
            String message = String.format("Expected equal types for '%s' comparison, found '%s' and '%s'",
                compare, left.outType(), right.outType()
            );
            Glob.ERR.kill(message);
        }
        if(!left.outType().isAllowedCompare(compare)){
            String message = String.format("Cannot compare %s and %s using %s",
                    left.outType(), right.outType(), compare
            );
            Glob.ERR.kill(message);
        }
    }
}
