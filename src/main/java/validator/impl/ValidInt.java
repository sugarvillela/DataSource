package validator.impl;

import attrib.iface.IValidProps;
import err.ERR_TYPE;
import validator.iface.IValid;

public class ValidInt implements IValid {
    private static ValidInt instance;

    public static ValidInt initInstance(){
        return (instance == null)? (instance = new ValidInt()): instance;
    }

    private ValidInt(){}

    @Override
    public ERR_TYPE validate(String text) {
        try{
            Integer.parseInt(text);
            return ERR_TYPE.NONE;
        }
        catch(Exception e){
            return ERR_TYPE.INVALID_INT;
        }
    }

    @Override
    public ERR_TYPE validate(IValidProps requester, String text) {
        return this.validate(text);
    }
}
