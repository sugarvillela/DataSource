package attrib.impl;

import attrib.iface.PropertyEnum;
import attrib.iface.IValidProps;
import utilfile.UtilFileProps;
import err.ERR_TYPE;
import validator.Validators;
import validator.iface.IValid;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/** The base class does the work, leaving hooks for the child class to override.
 *  -- Child class is defined anonymously within the enum kill containing the property
 *     names and default values.
 *  -- Child class only contains methods that require calling the enum kill by name.
 *  -- Other than the enum kill name, all child implementations are identical.
 */
public abstract class ValidPropsBase implements IValidProps {
    private final UtilFileProps utilFileProps;
    protected IValid validPropertyEnum;
    protected PropertyEnum propertyEnum;
    protected Properties properties;
    protected String key, val;

    public ValidPropsBase() {
        this.init();
        this.utilFileProps = UtilFileProps.initInstance();
    }

    /**Hook for child impl to kill validPropertyEnum from enum kill impl */
    protected abstract void init();

    @Override
    public ERR_TYPE put(String keyValPair) {
        ERR_TYPE errType;
        return((errType = Validators.VALID_KEY_VAL.validate(this, keyValPair)) == ERR_TYPE.NONE)
            ? this.put(key, val) : errType;
    }

    @Override
    public ERR_TYPE put(String key, String val) {
        ERR_TYPE errType;
        if(
            (errType = this.validPropertyEnum.validate(this, key)) == ERR_TYPE.NONE &&
            (errType = propertyEnum.validator().validate(val)) == ERR_TYPE.NONE
        ){
            properties.put(key, val);
        }
        return errType;
    }

    @Override
    public ERR_TYPE loadFromFile(String filePath) {
        Properties backup = new Properties();// restore prev state on fail
        backup.putAll(this.properties);

        Properties propsFromFile = new Properties();
        ERR_TYPE errType;
        if((errType = utilFileProps.propsFromFile(filePath, propsFromFile)) == ERR_TYPE.NONE){
            Set<Map.Entry<Object, Object>> entries = propsFromFile.entrySet();
            for (Map.Entry<Object, Object> entry : entries) {
                if((errType = this.put(entry.getKey().toString(), entry.getValue().toString())) != ERR_TYPE.NONE){
                    this.properties = backup;
                    break;
                }
            }
        }
        return errType;
    }

    @Override
    public ERR_TYPE saveToFile(String filePath) {
        return this.saveToFile(filePath, null);
    }

    @Override
    public ERR_TYPE saveToFile(String filePath, String comment) {
        return utilFileProps.propsToFile(filePath, properties, comment);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public void restoreDefaults() {
        this.properties = getDefaults();
    }

    @Override
    public void receive(String key, String val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public void receive(PropertyEnum propertyEnum) {
        this.propertyEnum = propertyEnum;
    }

    @Override
    public void display() {
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            System.out.printf("Attrib: %s = %s \n", entry.getKey().toString(), entry.getValue().toString());
        }
    }
}
