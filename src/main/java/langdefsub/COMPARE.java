package langdefsub;

public enum COMPARE {
    EQUAL   ('='),
    GT      ('>'),
    LT      ('<'),
    ;

    public final char asChar;

    COMPARE(char asChar){
        this.asChar = asChar;
    }

    public char toChar(){
        return asChar;
    }

    public static COMPARE fromChar(char ch){
        for(COMPARE op : values()){
            if(op.asChar == ch){
                return op;
            }
        }
        return null;
    }

    public static char[] allChars(){
        COMPARE[] values = values();
        char[] out = new char[values.length];
        for(int i = 0; i < values.length; i++){
            out[i] = values[i].toChar();
        }
        return out;
    }
}
