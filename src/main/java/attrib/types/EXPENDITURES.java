package attrib.types;

import attrib.iface.IValidProps;
import attrib.iface.PropertyEnum;
import attrib.impl.ValidPropsBase;
import validator.Validators;
import validator.iface.IValid;
import validator.impl.ValidPropertyEnum;

import java.util.Properties;

public enum EXPENDITURES implements PropertyEnum {
    TEQUILA         (Validators.VALID_INT,      "2000"),
    WINE            (Validators.VALID_INT,      "1700"),
    BEER            (Validators.VALID_INT,      "5000"),
    ENTERTAINMENT   (Validators.VALID_BOOL,     "true")
    ;

    private final IValid validator;
    private final String defValue;

    EXPENDITURES(IValid validator, String defValue) {
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
                return EXPENDITURES.valueOf(text);
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
            this.validPropertyEnum = EXPENDITURES.validPropertyEnum;
        }

        @Override
        public Properties getDefaults() {
            Properties defaultProperties = new Properties();
            for(PropertyEnum propertyEnum : EXPENDITURES.values()){
                defaultProperties.put(propertyEnum.defKey(), propertyEnum.defVal());
            }
            return defaultProperties;
        }
    };
}
