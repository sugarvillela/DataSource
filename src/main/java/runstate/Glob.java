package runstate;

import datasink.DataSink;
import datasource.factory.FactoryDataSource;
import utilfile.FileNameUtil;
import utilfile.SmallFileDump;
import err.iface.IErr;
import err.impl.Err;
import err.impl.ErrDev;
import langdef.util.EnumsByType;
import pushpoputil.impl.PushPopUtil;
import runstate.impl.RunState;

public class Glob {
    public static final boolean             DISPLAY_DEV_ERRORS =    true;   // disable for release?
    public static final boolean             STACK_TRACE_ON_ERR =    true;   // disable for release?
    public static final int                 STACK_TRACE_DISP_N =    7;      // how many stack trace items to display
    public static final IErr                ERR_DEV =               ErrDev.initInstance();
    public static final IErr                ERR =                   Err.initInstance();

    public static final String              NULL_TEXT =             "-";    // for csv strings

    public static final FileNameUtil        FILE_NAME_UTIL =        FileNameUtil.initInstance();
    public static final SmallFileDump       SMALL_FILE_DUMP =       SmallFileDump.initInstance();
    public static final FactoryDataSource   FACTORY_DATA_SOURCE =   FactoryDataSource.initInstance();

    public static final RunState            RUN_STATE =             RunState.initInstance();
    //public static final ReadNodeFactory     READ_NODE_FACTORY =     ReadNodeFactory.initInstance();
    public static final EnumsByType         ENUMS_BY_TYPE =         EnumsByType.initInstance();
    public static final PushPopUtil         PUSH_POP_UTIL =         PushPopUtil.initInstance();
    public static final DataSink            DATA_SINK =             DataSink.initInstance();
    //public static final DataSink            DATA_SINK =             DataSink.initInstance();

}
