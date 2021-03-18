package validator;

import validator.impl.ValidBool;
import validator.impl.ValidInt;
import validator.impl.ValidKeyVal;
import validator.impl.ValidString;

public abstract class Validators {
    public static final ValidBool VALID_BOOL = ValidBool.initInstance();
    public static final ValidInt VALID_INT = ValidInt.initInstance();
    public static final ValidString VALID_STRING = ValidString.initInstance();
    public static final ValidKeyVal VALID_KEY_VAL = ValidKeyVal.initInstance();
}
