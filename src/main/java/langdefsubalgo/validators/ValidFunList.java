package langdefsubalgo.validators;

import langdefsub.COMPARE;
import langdefsub.FUN_TYPE;
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
        for(PAR_TYPE parType : caller.allowedParamTypes()){
            if(parType == actual){
                return;
            }
        }
        Glob.ERR.kill(
                String.format("Wrong parameter type '%s' for sub-language function '%s'", actual, caller.funType()),
                String.format("Proper usage: %s", caller.description())
        );
    }

    public void validateRangeLength(IFun caller, int[] numbers){
        if((numbers.length != 2)){
            Glob.ERR.kill(
                    String.format("Expected range in format RANGE(n:m) or RANGE(:m) or RANGE(n:), found %s", Arrays.toString(numbers)),
                    String.format("Proper usage: %s", caller.description())
            );
        }
    }
    public void validateRange(IFun caller, int[] numbers){
        int prev = -1024;
        for(int i = 0; i < numbers.length; i++){
            if(prev > numbers[i]){
                Glob.ERR.kill(
                        String.format("Expected parameters in ascending order, found %d:%d", numbers[0], numbers[1]),
                        String.format("Proper usage: %s", caller.description())
                );
            }
            prev = numbers[i];
        }
    }

    public void validatePrimTypeChain(IFun prev, IFun curr){
        PRIM_TYPE actual = (prev == null)? PRIM_TYPE.NULL : prev.primTypeAfter();
        PRIM_TYPE expected = curr.primTypeBefore();
        if(expected != null && actual != expected){
            Glob.ERR.kill(
                    String.format("Expected input type %s for sub-language function %s, found %s",
                            expected, curr.funType(), actual
                    ),
                    String.format("Proper usage: %s", curr.description())
            );
        }
    }

    // types before and after: null means don't care; empty means disallow
    public void validateFunTypeChain(IFun prev, IFun curr){
        FUN_TYPE[] expectedList = curr.funTypesBefore();
        if(expectedList != null && prev != null){
            FUN_TYPE actual = prev.funType();
            if(!inList(actual, expectedList)){
                String errMessage = (expectedList.length == 0)?
                        String.format("Expected sub-language function %s first in list, found %s",
                                curr.funType(), prev.funType()
                        )
                        :
                        String.format("Expected function type(s) %s to precede sub-language function %s, found %s",
                                Arrays.toString(expectedList), curr.funType(), actual
                        );
                Glob.ERR.kill(errMessage, String.format("Proper usage: %s", curr.description()));
            }
        }
    }

    // types before and after: null means don't care; empty means disallow
    public void validateRangeBeforeInChain(IFun prev, IFun curr){
        PAR_TYPE[] expectedList = curr.parTypesBefore();
        if(expectedList != null && prev != null){
            PAR_TYPE actual = prev.parType();
            if(!inList(actual, expectedList)){
                String errMessage = (expectedList.length == 0)?
                        String.format("Expected sub-language function %s first in list, found %s",
                                curr.funType(), prev.funType()
                        )
                        :
                        String.format("Expected param type(s) %s to precede sub-language function %s, found %s",
                                Arrays.toString(expectedList), curr.funType(), actual
                        );
                Glob.ERR.kill(errMessage, String.format("Proper usage: %s", curr.description()));
            }
        }
    }

    private boolean inList(FUN_TYPE actual, FUN_TYPE[] expectedList){
        for(FUN_TYPE expected : expectedList){
            if(expected == actual){
                return true;
            }
        }
        return false;   // always false on empty list
    }
    private boolean inList(PAR_TYPE actual, PAR_TYPE[] expectedList){
        for(PAR_TYPE expected : expectedList){
            if(expected == actual){
                return true;
            }
        }
        return false;   // always false on empty list
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
