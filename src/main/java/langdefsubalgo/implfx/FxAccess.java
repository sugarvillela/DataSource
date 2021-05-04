package langdefsubalgo.implfx;

import langdefsub.WordTraitRuleByStep;
import langdefsubalgo.iface.IFun;
import langdefsubalgo.impl.FunBase;
import rule_wordtrait.WordTraitRule;
import runstate.Glob;

import java.util.Arrays;

import static langdefsub.FUN_TYPE.*;
import static langdefsub.PAR_TYPE.*;
import static langdefsub.PRIM_TYPE.*;

public class FxAccess extends FunBase {
    private final boolean accessMod;
    private int[] numbers;
    private String[] strings;

    public FxAccess(IFun prev, String text, boolean accessMod) {
        super(prev);
        this.setFunType(GET_ACCESS);
        this.setPrimTypeBefore(NULL);
        this.setPrimTypeAfter(NULL);
        this.setParamTypes(NUM_PAR, NUM_PAR_NEG, NUM_RANGE, NUM_LIST, NUM_LIST_NEG);
        this.setFunTypesBefore();
        this.setParTypesBefore();

        WordTraitRule accessTypeRule =  WordTraitRuleByStep.initInstance().getStep_accessType();

        this.accessMod = accessMod;
        Glob.ERR.check(accessTypeRule.tryParse(this, text));

        if(parTypeEnum == NUM_RANGE){
            Glob.VALID_FUN_LIST.validateRangeLength(this, numbers);
            Glob.VALID_FUN_LIST.validateRange(this, numbers);
        }
    }

    @Override
    public String description() {
        return String.format("%s -> [%s] -> various, params: %s",
                this.primTypeBefore(), this.funType(), Arrays.toString(paramTypes)
        );
    }

    /*=====IWordTraitClient methods not implemented by FunBase========================================================*/

    @Override
    public void receiveContent(String... content) {
        strings = content;
    }

    @Override
    public void receiveContent(int... content) {
        numbers = content;
    }

    @Override
    public String[] stringContent() {
        return strings;
    }

    @Override
    public int[] intContent() {
        return numbers;
    }

    /*=====Object methods=============================================================================================*/

    @Override
    public String toString() {
        return "FxAccess{" +
                "accessMod=" + accessMod +
                ", strings=" + Arrays.toString(strings) +
                ", numbers=" + Arrays.toString(numbers) +
                ", parTypeEnum=" + parTypeEnum +
                "\n" + this.description() +
                "\n}";
    }
}
