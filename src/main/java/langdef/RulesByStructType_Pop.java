package langdef;

import rule_pop.impl.PopRule;

import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LOOKUP.ID_ACCESS;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_NON_KEYWORD.FX_WORD;
import static langdef.STRUCT_SYMBOL.*;

public class RulesByStructType_Pop {
    public void initRules() {

        /*=====STRUCT_KEYWORD=========================================================================================*/

        ATTRIB.getPopRule().setRules(PopRule.popOnBadPush());

        CONSTANT.getPopRule().setRules(PopRule.popOnTimeOut(1));

        RX.getPopRule().setRules(PopRule.popOnBadPush());

        FX.getPopRule().setRules(PopRule.popOnBadPush());

        FUN.getPopRule().setRules(PopRule.popOnCmd());

        SCOPE.getPopRule().setRules(PopRule.popOnCmd());

        IF.getPopRule().setRules(PopRule.popOnCmd());

        ELSE.getPopRule().setRules(PopRule.popOnCmd());

        //INCLUDE.getPopRule().setRules();

        /*=====STRUCT_LOOKUP==========================================================================================*/

        ID_ACCESS.getPopRule().setRules(PopRule.popOnTimeOut(1));

        /*=====STRUCT_NON_KEYWORD=====================================================================================*/

        LANG_T.getPopRule().setRules();

        IF_TEST.getPopRule().setRules(PopRule.popOnTimeOut(1));

        SCOPE_TEST.getPopRule().setRules(PopRule.popOnTimeOut(1));

        CONDITIONAL_ITEM.getPopRule().setRules(PopRule.popOnTimeOut(1));

        RX_WORD.getPopRule().setRules(PopRule.popOnCmd());

        FX_WORD.getPopRule().setRules(PopRule.popOnCmd());

        /*=====STRUCT_SYMBOL==========================================================================================*/

        LANG_S.getPopRule().setRules(PopRule.popOnCmd(), PopRule.errOnBadPush());

        LANG_T_INSERT.getPopRule().setRules(PopRule.popOnCmd());

        ANTI_FX.getPopRule().setRules(PopRule.popOnBadPush());
    }
}
