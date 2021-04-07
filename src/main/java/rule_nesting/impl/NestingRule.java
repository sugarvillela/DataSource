package rule_nesting.impl;

import langdefalgo.iface.LANG_STRUCT;
import rule_nesting.iface.INestingRule;

import java.util.ArrayList;

public class NestingRule implements INestingRule {
    //private final LANG_STRUCT parentEnum;
    private LANG_STRUCT[] allowedNesting;
    private boolean isCopyOnly;

    public NestingRule() {
        //this.parentEnum = parentEnum;// for debug
    }

    @Override
    public boolean isAllowedNesting(LANG_STRUCT langStruct){
        int len = allowedNesting.length;
        for(int i = 0; i < len; i++){
            if(allowedNesting[i].equals(langStruct)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCopyOnly() {// only for enums with mandatory pop pattern e.g. END_FUN
        return isCopyOnly;
    }

    @Override
    public void setAllowedNesting(ArrayList<LANG_STRUCT> allowedNesting) {
        this.allowedNesting = allowedNesting.toArray(allowedNesting.toArray(new LANG_STRUCT[0]));
    }

    @Override
    public void setAllowedNesting(LANG_STRUCT... allowedNesting) {
        this.allowedNesting = allowedNesting;
    }

    @Override
    public void setCopyOnly(boolean isCopyOnly) {
        this.isCopyOnly = isCopyOnly;
    }
}
