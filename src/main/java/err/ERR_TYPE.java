package err;

/** If resources are scarce, a switch for messaging would be better;
 * Instantiating all messages up front violates the 'plan for the common case' rule */

public enum ERR_TYPE {
    NONE            ("", false),

    // Attrib errors
    NOT_KEY_VAL         ("Expected 'key=value' format"),
    UNKNOWN_KEY         ("Unknown attribute key"),
    INVALID_BOOL        ("Expected true/false boolean"),
    INVALID_INT         ("Expected integer value"),
    INVALID_STRING      ("Expected non-empty text"),

    // Identifier errors
    MISPLACED_ID        ("Identifier not allowed here"),
    MISSING_ID          ("Expected identifier after keyword/symbol"),
    DUPLICATE_ID        ("Identifier already exists"),
    UNKNOWN_ID          ("Identifier is not defined"),

    // Structure/syntax errors
    UNKNOWN_LANG_STRUCT ("Expected a valid keyword/symbol"),
    CONSTANT_NOT_SINGLE ("Expected a single item here"),
    NO_ENCLOSING_BRACES ("Expected enclosing braces here"),

    // Scope/if errors
    BAD_TEST_PARAM      ("Invalid SCOPE/IF parameter; use one-word scope or RX, with parentheses"),
    MISSING_CONDITIONAL ("Expected a conditional expression"),

    // Sublang errors
    UNKNOWN_PATTERN     ("Unknown pattern"),
    UNKNOWN_FUNCTION    ("Not a valid RxFx sub-language function"),

    DEV_ERROR           ("Developer error"),
    SYNTAX              ("Syntax error"),
    FILE_ERROR          ("Something went wrong with a file"),
    ;

    private final String message;
    private final boolean isErr;

    ERR_TYPE() {
        this.message = null;
        this.isErr = true;
    }
    ERR_TYPE(String message, boolean isErr) {
        this.message = message;
        this.isErr = isErr;
    }
    ERR_TYPE(String message) {
        this.message = message;
        this.isErr = true;
    }
    
    public String message(){
        return (message == null)? this.toString() : message;
    }
    public boolean isErr(){
        return isErr;
    }
}
