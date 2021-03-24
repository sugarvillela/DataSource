package langdef;

import runstate.Glob;

import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.*;

/** Initialize these at run start to eliminate self-reference error on static enum creation */
public class RulesByStructType_Nesting {
    public void initRules() {

        /*=====STRUCT_KEYWORD=========================================================================================*/

        ATTRIB.getNestingRule().setAllowedNesting();

        CONSTANT.getNestingRule().setAllowedNesting(Glob.ENUMS_BY_TYPE.allFrontEndLangStruct());

        RX.getNestingRule().setAllowedNesting(LANG_T_INSERT);

        FX.getNestingRule().setAllowedNesting();

        FUN.getNestingRule().setCopyOnly(true);
        FUN.getNestingRule().setAllowedNesting();

        SCOPE.getNestingRule().setAllowedNesting(SCOPE_TEST, SCOPE, RX, FX);

        IF.getNestingRule().setAllowedNesting(IF_TEST, RX, FX);

        ELSE.getNestingRule().setAllowedNesting(
                RX, FX, IF, FUN//, IF_ELSE)
        );

        INCLUDE.getNestingRule().setAllowedNesting();

        /*=====STRUCT_LOOKUP==========================================================================================*/

        ID_ACCESS.getNestingRule().setAllowedNesting();

        /*=====STRUCT_NON_KEYWORD=====================================================================================*/

        LANG_T.getNestingRule().setAllowedNesting(
                LANG_S
        );

        IF_TEST.getNestingRule().setAllowedNesting(
                RX
        );

        SCOPE_TEST.getNestingRule().setAllowedNesting(
                RX, CONDITIONAL_ITEM
        );

        CONDITIONAL_ITEM.getNestingRule().setAllowedNesting(
                IF, ELSE
        );

        RX_WORD.getNestingRule().setAllowedNesting();

        FX_WORD.getNestingRule().setAllowedNesting();

        /*=====STRUCT_SYMBOL==========================================================================================*/

        LANG_S.getNestingRule().setAllowedNesting(
                INCLUDE, FUN, CONSTANT, ID_ACCESS, ATTRIB, RX, FX, SCOPE, IF, ELSE//,
                //LIST_SCOPES, LIST_STRING, LIST_NUMBER, LIST_BOOLEAN, LIST_VOTE, LIST_DISCRETE
        );

        LANG_T_INSERT.getNestingRule().setCopyOnly(true);
        LANG_T_INSERT.getNestingRule().setAllowedNesting();

        ANTI_FX.getNestingRule().setAllowedNesting();
    }
}