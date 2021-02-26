package datasource;

import datasource.factory.FactoryDataSource;
import datasource.util.FileNameUtil;
import datasource.util.SmallFileDump;

import java.io.File;

public class LocalStatics {
    public static final FileNameUtil FILE_NAME_UTIL = FileNameUtil.initInstance();
    public static final SmallFileDump SMALL_FILE_DUMP = SmallFileDump.initInstance();
    public static final FactoryDataSource FACTORY_DATA_SOURCE = FactoryDataSource.initInstance();


}
