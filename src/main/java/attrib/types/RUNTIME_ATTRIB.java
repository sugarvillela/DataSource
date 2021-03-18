package attrib.types;

import attrib.iface.PropertyEnum;
import attrib.iface.IValidProps;
import attrib.impl.ValidPropsBase;
import validator.Validators;
import validator.iface.IValid;
import validator.impl.ValidPropertyEnum;

import java.util.Properties;

public enum RUNTIME_ATTRIB implements PropertyEnum {
    PROJ_NAME       (Validators.VALID_STRING,   "MyProject"),
    SOME_BOOLEAN    (Validators.VALID_BOOL,     "true"),
    SOME_NUMBER     (Validators.VALID_INT,      "75")
    ;

    private final IValid validator;
    private final String defValue;

    RUNTIME_ATTRIB(IValid validator, String defValue) {
        this.validator = validator;
        this.defValue = defValue;
    }

    @Override
    public String defKey() {
        return this.toString();
    }

    @Override
    public String defVal() {
        return this.defValue;
    }

    @Override
    public IValid validator() {
        return this.validator;
    }

    public static IValid validPropertyEnum = new ValidPropertyEnum() {
        @Override
        protected PropertyEnum propertyEnumFromString(String text) {
            try{
                return RUNTIME_ATTRIB.valueOf(text);
            }
            catch(IllegalArgumentException | NullPointerException e){
                return null;
            }
        }
    };

    public static IValidProps props = new ValidPropsBase() {
        @Override
        protected void init() {
            this.properties = getDefaults();
            this.validPropertyEnum = RUNTIME_ATTRIB.validPropertyEnum;
        }

        @Override
        public Properties getDefaults() {
            Properties defaultProperties = new Properties();
            for(PropertyEnum propertyEnum : RUNTIME_ATTRIB.values()){
                defaultProperties.put(propertyEnum.defKey(), propertyEnum.defVal());
            }
            return defaultProperties;
        }
    };
}
