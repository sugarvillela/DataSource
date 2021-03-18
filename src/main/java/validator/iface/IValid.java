package validator.iface;

import attrib.iface.IValidProps;
import err.ERR_TYPE;

public interface IValid {
    ERR_TYPE validate(String text);
    ERR_TYPE validate(IValidProps requester, String text);
}
