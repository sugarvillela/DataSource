package validator.impl;

import attrib.iface.IValidProps;
import err.ERR_TYPE;
import validator.iface.IValid;

public class ValidString implements IValid {
    private static ValidString instance;

    public static ValidString initInstance(){
        return (instance == null)? (instance = new ValidString()): instance;
    }

    private ValidString(){}

    @Override
    public ERR_TYPE validate(String text) {
        return (text == null || text.isEmpty()) ? ERR_TYPE.INVALID_STRING : ERR_TYPE.NONE;
    }

    @Override
    public ERR_TYPE validate(IValidProps requester, String text) {
        return this.validate(text);
    }
}
