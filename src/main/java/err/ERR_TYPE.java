package err;

/** If resources are scarce, a switch for messaging would be better;
 * Instantiating all messages up front violates the 'plan for the common case' rule */

public enum ERR_TYPE {
    NONE            ("", false),

    // Attrib errors
    NOT_KEY_VAL         ("Expected 'key=value' format"),
    UNKNOWN_KEY         ("Unknown key"),
    INVALID_BOOL        ("Expected true/false boolean"),
    INVALID_INT         ("Expected integer value"),
    INVALID_STRING      ("Expected non-empty text"),

    // Identifier errors
    MISPLACED_ID        ("Keyword should not be followed by an identifier"),
    MISSING_ID          ("Expected identifier after keyword"),
    DUPLICATE_ID        ("Identifier already exists"),
    UNKNOWN_ID          ("Identifier is not defined"),

    UNKNOWN_LANG_STRUCT ("Expected a valid keyword/language structure"),
    BAD_TEST_PARAM      ("Invalid SCOPE or IF parameter; use a one-word scope or RX"),

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
