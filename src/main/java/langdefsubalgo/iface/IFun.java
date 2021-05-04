package langdefsubalgo.iface;

import langdefsub.FUN_TYPE;
import langdefsub.PAR_TYPE;
import langdefsub.PRIM_TYPE;
import rule_wordtrait.iface.IWordTraitClient;

public interface IFun extends IWordTraitClient {
    String description();
    FUN_TYPE funType();
    PAR_TYPE[] allowedParamTypes();

    PRIM_TYPE primTypeBefore(); // null means don't care; empty means disallow
    PRIM_TYPE primTypeAfter();  // null means don't care; empty means disallow

    FUN_TYPE[] funTypesBefore();// null means don't care; empty means disallow
    PAR_TYPE[] parTypesBefore();// null means don't care; empty means disallow
}
