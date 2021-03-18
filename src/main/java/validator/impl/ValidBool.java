package validator.impl;

import attrib.iface.IValidProps;
import err.ERR_TYPE;
import validator.iface.IValid;

/** Of note: Boolean.parseBool() does not throw exception */
public class ValidBool implements IValid {
    private static ValidBool instance;

    public static ValidBool initInstance(){
        return (instance == null)? (instance = new ValidBool()): instance;
    }

    private ValidBool(){}

    @Override
    public ERR_TYPE validate(String text) {
        return ("true".equals(text) || "false".equals(text))
                ? ERR_TYPE.NONE : ERR_TYPE.INVALID_BOOL;
    }

    @Override
    public ERR_TYPE validate(IValidProps requester, String text) {
        return this.validate(text);
    }
}
