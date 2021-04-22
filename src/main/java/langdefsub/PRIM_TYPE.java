package langdefsub;

public enum PRIM_TYPE {
    BOOLEAN    (COMPARE.EQUAL),
    DISCRETE   (COMPARE.GT, COMPARE.LT, COMPARE.EQUAL),
    NUMBER     (COMPARE.GT, COMPARE.LT, COMPARE.EQUAL),
    STRING     (COMPARE.EQUAL),
    NULL       (COMPARE.EQUAL),
    STATE      (COMPARE.EQUAL),
    IMMUTABLE  (COMPARE.EQUAL),
    ;

    private final COMPARE[] allowedCompares;

    PRIM_TYPE(COMPARE... allowedCompares) {
        this.allowedCompares = allowedCompares;
    }
}
