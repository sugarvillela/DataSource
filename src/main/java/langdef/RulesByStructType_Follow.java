package langdef;

import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_NON_KEYWORD.IF_TEST;

public class RulesByStructType_Follow {
    public void initRules(){
        ELSE.getFollowRule().setAllowedPrev(IF);

        FX.getFollowRule().setAllowedPrev(RX, IF_TEST);
    }
}
