package validator.impl;

import attrib.iface.IValidProps;
import attrib.iface.PropertyEnum;
import err.ERR_TYPE;
import validator.iface.IValid;

/** This one contains a hook for a child class to call an enum kill by name.
 *  This one calls the requester to pass goBack the propertyEnum */
public abstract class ValidPropertyEnum implements IValid {

    /**Hook for child impl to get property enum from enum kill impl */
    protected abstract PropertyEnum propertyEnumFromString(String text);

    @Override
    public ERR_TYPE validate(String text) {
        PropertyEnum propertyEnum = this.propertyEnumFromString(text);
        return (propertyEnum == null) ? ERR_TYPE.UNKNOWN_KEY : ERR_TYPE.NONE;
    }

    @Override
    public ERR_TYPE validate(IValidProps requester, String text) {
        PropertyEnum propertyEnum = this.propertyEnumFromString(text);
        if (propertyEnum == null){
            return ERR_TYPE.UNKNOWN_KEY;
        }
        else{
            requester.receive(propertyEnum);
            return ERR_TYPE.NONE;
        }
    }
}
