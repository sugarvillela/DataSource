package textpattern;

public enum TEXT_PATTERN {
    INSERT,
    RX,
    FX
    ;

    public static TEXT_PATTERN getEnum(String text){
        try{
            return valueOf(text);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }
}
