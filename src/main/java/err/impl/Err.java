package err.impl;

import err.iface.IErr;
import runstate.Glob;

/** For transpiler errors and warnings: always displays
 *  Stack trace enabled by Glob.STACK_TRACE_ON_ERR */
public class Err extends ErrDev{
    private static IErr instance;

    public static IErr initInstance(){
        return (instance == null)? (instance = new Err()) : instance;
    }

    private Err(){
        displayErrors = true;
        displayStackTrace = Glob.STACK_TRACE_ON_ERR;
    }
}
