package datasource.core;

import static datasource.LocalStatics.FILE_NAME_UTIL;
import static datasource.LocalStatics.SMALL_FILE_DUMP;

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
