package validator.impl;

import attrib.iface.IValidProps;
import err.ERR_TYPE;
import validator.iface.IValid;

/** This one calls the requester to pass goBack the tokenized key and val */
public class ValidKeyVal implements IValid {
    private static ValidKeyVal instance;

    public static ValidKeyVal initInstance(){
        return (instance == null)? (instance = new ValidKeyVal()): instance;
    }

    private ValidKeyVal(){}

    @Override
    public ERR_TYPE validate(String text) {
        String[] tok;
        if(
            text != null &&
            (tok = text.split("=")).length == 2 &&
            !tok[0].isEmpty() &&
            !tok[1].isEmpty()
        ){
            return ERR_TYPE.NONE;
        }
        return ERR_TYPE.NOT_KEY_VAL;
    }

    @Override
    public ERR_TYPE validate(IValidProps requester, String text) {
        String[] tok;
        if(
            text != null &&
            (tok = text.split("=")).length == 2 &&
            !tok[0].isEmpty() &&
            !tok[1].isEmpty()
        ){
            requester.receive(tok[0], tok[1]);
            return ERR_TYPE.NONE;
        }
        return ERR_TYPE.NOT_KEY_VAL;
    }
}
