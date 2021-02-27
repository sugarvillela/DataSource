package datasource.core;

import static runstate.Glob.FILE_NAME_UTIL;
import static runstate.Glob.SMALL_FILE_DUMP;

public class SourceFile extends SourceList {
    public SourceFile(String fileName) {
        super(SMALL_FILE_DUMP.toList(fileName));
        this.stringIdentifier = FILE_NAME_UTIL.getFileNameFromPath(fileName);
    }

    /** Override list impl because unique suffix not needed for file name
     * @return short file name */
    @Override
    protected String getIdentifier() {
        return stringIdentifier;
    }
}
