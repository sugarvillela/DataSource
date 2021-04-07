package rule_follow.impl;

import langdefalgo.iface.LANG_STRUCT;
import rule_follow.iface.IFollowRule;

import java.util.Arrays;

public class FollowRule implements IFollowRule {
    private LANG_STRUCT[] allowedPrev;

    @Override
    public boolean allAreAllowed() {
        return (allowedPrev == null);
    }

    @Override
    public boolean isAllowedPrev(LANG_STRUCT prevLangStruct) {
        for(int i = 0; i < allowedPrev.length; i++){
            if(allowedPrev[i].equals(prevLangStruct)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void setAllowedPrev(LANG_STRUCT... allowedPrev) {
        this.allowedPrev =  allowedPrev;
    }

    @Override
    public String csvString() {
        return (allowedPrev == null)? "any" : Arrays.toString(allowedPrev);
    }
}
