package err.impl;

import err.ERR_TYPE;
import err.iface.IErr;
import err.ut.StackTraceUtil;
import runstate.Glob;
import readnode.iface.IReadNode;

import java.util.ArrayList;

/** For development errors: quit or complain, selectable at call
 *  Disable everything by setting Glob.DISPLAY_DEV_ERRORS false
 *  Stack trace displays with message */
public class ErrDev implements IErr {
    private static IErr instance;

    public static IErr initInstance(){
        return (instance == null)? (instance = new ErrDev()) : instance;
    }

    protected ErrDev(){
        displayErrors = Glob.DISPLAY_DEV_ERRORS;
        displayStackTrace = true;
    }

    protected static final String TAB = "\t| ";
    protected String[] errInfo;
    protected boolean displayErrors, displayStackTrace;

    @Override
    public boolean check(ERR_TYPE errType) {
        if(errType.isErr()){
            this.kill(errType);
            return false;
        }
        return true;
    }

    @Override
    public void kill(ERR_TYPE errType) {
        this.kill(errType, Glob.RUN_STATE.getCurrNode());
    }

    @Override
    public void kill(ERR_TYPE errType, IReadNode statusNode) {
        if(displayErrors){
            populateErr(
                    statusNode,
                    errType.message(),
                    null
            );
            dispAndQuit();
        }
    }

    @Override
    public void kill(ERR_TYPE errType, String message) {
        if(displayErrors){
            populateErr(
                    null,
                    errType.message(),
                    message
            );
            dispAndQuit();
        }
    }

    @Override
    public void kill(String message){
        this.kill(message, null);
    }

    @Override
    public void kill(String message, String text){
        if(displayErrors){
            populateErr(Glob.RUN_STATE.getCurrNode(), message, text);
            dispAndQuit();
        }
    }

    @Override
    public void kill(IReadNode statusNode, String message){
        if(Glob.DISPLAY_DEV_ERRORS){
            populateErr(statusNode, message, null);
            dispAndQuit();
        }
    }

    protected final void populateErr(IReadNode readNode, String message, String text){
        errInfo = new String[5];
        if(readNode != null){
            errInfo[0] = (readNode.friendlyStatusString());
            errInfo[2] = ("in '" + readNode.containerText() + "'");
            errInfo[3] = ("at '" + readNode.text() + "'");
        }
        if(message != null){
            errInfo[1] = (message);
        }
        if(text != null){
            errInfo[4] = (": '" + text + "'");
        }
    }

    private void dispAndQuit(){
        System.out.println( "\n" + TAB + "=====ERROR=================================================" );
        for(String item : errInfo){
            if(item != null){
                System.out.println(TAB + item);
            }
        }
        if(displayStackTrace){
            System.out.println("\n" + TAB + "STACK TRACE...");
            ArrayList<String> stackTrace = StackTraceUtil.getInstance().shortTrace();
            int len = Math.min(stackTrace.size(), Glob.STACK_TRACE_DISP_N);
            for(int i = 0; i < len; i++){
                System.out.println(TAB + stackTrace.get(i));
            }
        }
        System.out.println( TAB + "===========================================================" );
        System.exit(0);
    }
}
