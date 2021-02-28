package runstate;

import datasource.factory.FactoryDataSource;
import datasource.util.FileNameUtil;
import datasource.util.SmallFileDump;
import langdef.util.EnumsByType;
import langdef.util.LangStructUtil;

public class Glob {

    public static final FileNameUtil FILE_NAME_UTIL = FileNameUtil.initInstance();
    public static final SmallFileDump SMALL_FILE_DUMP = SmallFileDump.initInstance();
    public static final FactoryDataSource FACTORY_DATA_SOURCE = FactoryDataSource.initInstance();
    public static final LangStructUtil LANG_STRUCT_UTIL = LangStructUtil.initInstance();

    public static final EnumsByType ENUMS_BY_TYPE = EnumsByType.initInstance();
}
