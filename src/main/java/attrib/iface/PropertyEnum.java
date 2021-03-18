package attrib.iface;

import validator.iface.IValid;

/** Interface for enum sets in 'types' package */
public interface PropertyEnum {
    String defKey();
    String defVal();
    IValid validator();
}
