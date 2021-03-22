package rule_nesting.impl;

import langdefalgo.iface.LANG_STRUCT;
import rule_nesting.iface.INestingRule;

import java.util.ArrayList;

public class NestingRule implements INestingRule {
    private LANG_STRUCT[] allowedNesting;
    private boolean isCopyOnly;

    @Override
    public boolean isAllowedNesting(LANG_STRUCT langStruct){
        for(int i = 0; i < allowedNesting.length; i++){
            if(allowedNesting[i].equals(langStruct)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCopyOnly() {
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
