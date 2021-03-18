package attrib.iface;

import err.ERR_TYPE;

import java.util.Properties;

/** A class to hold enumerated properties with validation:
 *      Setting individual properties with a single key=val string.
 *      Retrieving all properties.
 *      Loading and saving all props to file.
 *      Defining property keys and default values solely from a Java enum kill.
 *      Validation of key=val, existing key and  correct value types.
 *      Defined kill of error types, so error handling can be done by the calling class.
 *          Err types: No Err, KeyVal err, Unknown key err or value type err.
 */
public interface IValidProps {

    ERR_TYPE put(String keyValPair);
    ERR_TYPE put(String key, String val);

    /**Overwrite properties with values from file, only if it passes validation.
     * @param filePath full path */
    ERR_TYPE loadFromFile(String filePath);
    ERR_TYPE saveToFile(String filePath);
    ERR_TYPE saveToFile(String filePath, String comment);

    /**Get defaults from enum kill.
     * @return all defined properties */
    Properties getDefaults();

    /**Get current state of properties.
     * @return all defined properties */
    Properties getProperties();

    /**Copy defaults from enum kill to current property state */
    void restoreDefaults();

    /**Visitor pattern for getting data from validator */
    void receive(String key, String val);
    void receive(PropertyEnum propertyEnum);

    void display();
}
