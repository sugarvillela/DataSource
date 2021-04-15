package langdef;

/** Static constants relating to the language definition */
public abstract class LangConstants {
    public static final String SOURCE_FILE_EXTENSION = ".rxfx";
    public static final String LEX_FILE_EXTENSION = ".rxlx";



    // Trigger patterns for pushing lang structures: defines some language behavior
    public static final String OPEN_S = "/*$";      // pushes source datatype
    public static final String CLOSE_S = "$*/";     // pops all source datatypes
    public static final String OPEN_T = "*/";       // inserts target language without popping source
    public static final String CLOSE_T = "/*";      // pops target language insert
    public static final String SYM_A_FX = "!FX";     // like an else clause
    public static final String SYM_END_A_FX = "END!FX";// should not have to use

    public static final char COMMENT_START = '#';//
    public static final char DEFINE_START = '$';     // identifier declaration
    public static final char ACCESS_START = '*';     // identifier access

    public static final String ITEM_OPEN = "{";     // surrounds item content
    public static final String ITEM_CLOSE = "}";    // ends item content
    public static final String COND_OPEN = "(";     // surrounds conditional content
    public static final String COND_CLOSE = ")";    // ends conditional content


    public static final String CONT_LINE = "...";   // Matlab-like extension
    public static final String ACCESS_MOD = "*";    // FX access: input string instead of rx string
    public static final String TARG = "TARG";       // Specify target-language-style regex for RX
    public static final char   PATH_TREE_SEP = '.'; // for STRUCT_LIST_TYPE, RX, FX tree paths
}
