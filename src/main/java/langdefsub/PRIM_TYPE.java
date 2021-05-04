package langdefsub;

public enum PRIM_TYPE {
    BOOLEAN     (COMPARE.EQUAL),
    DISCRETE    (COMPARE.GT, COMPARE.LT, COMPARE.EQUAL),
    NUMBER      (COMPARE.GT, COMPARE.LT, COMPARE.EQUAL),
    STRING      (COMPARE.EQUAL),
    LIST        (COMPARE.EQUAL),
    NULL        (COMPARE.EQUAL),
    STATE       (COMPARE.EQUAL),
    IMMUTABLE   (COMPARE.EQUAL),
    PATH        (COMPARE.EQUAL), // a path to a list type
    ACCESS      ()               // output from [1:2] fxAccess
    ;

    private final COMPARE[] allowedCompares;

    PRIM_TYPE(COMPARE... allowedCompares) {
        this.allowedCompares = allowedCompares;
    }
    public boolean isAllowedCompare(COMPARE compare){
        for(COMPARE allowed : allowedCompares){
            if(allowed == compare){
                return true;
            }
        }
        return false;
    }
}
