package rule_follow.iface;

import langdefalgo.iface.LANG_STRUCT;

public interface IFollowRule {
    boolean allAreAllowed();
    boolean isAllowedPrev(LANG_STRUCT prevLangStruct);
    void setAllowedPrev(LANG_STRUCT... allowedPrev);
    String csvString();
}
