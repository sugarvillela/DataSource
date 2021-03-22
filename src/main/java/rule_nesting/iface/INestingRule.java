package rule_nesting.iface;

import langdefalgo.iface.LANG_STRUCT;

import java.util.ArrayList;

public interface INestingRule {
    boolean isAllowedNesting(LANG_STRUCT langStruct);   // true if arg is in list of allowed nesting
    boolean isCopyOnly();                               // should ignore all lang struct found in scope

    void setAllowedNesting(ArrayList<LANG_STRUCT> allowedNesting);
    void setAllowedNesting(LANG_STRUCT... allowedNesting);

    void setCopyOnly(boolean isCopyOnly);
}
