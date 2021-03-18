package err.ut;

import java.util.ArrayList;

public class StackTraceUtil {
    private static StackTraceUtil instance;

    private StackTraceUtil(){}

    public static StackTraceUtil getInstance(){
        return (instance == null)? (instance = new StackTraceUtil()): instance;
    }

    public ArrayList<String> shortTrace(){
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        ArrayList<String> out = new ArrayList<>();
        if( trace==null){
            out.add("No stack trace" );
        }
        else{
            int curr = 0;
            String format = "%s", skipText = "err";
            for(int i = 0; i<trace.length; i++){
                if(String.format(format, trace[i]).startsWith(skipText)){
                    curr = i;
                    break;
                }
            }
            for(int i = curr + 1; i<trace.length; i++){
                if(!String.format(format, trace[i]).startsWith(skipText)){
                    curr = i;
                    break;
                }
            }
            for(int i = curr; i<trace.length; i++){
                out.add(String.format(format, trace[i]));
            }
        }
        return out;
    }

    public ArrayList<String> longTrace(){
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        ArrayList<String> out = new ArrayList<>();
        if( trace==null){
            out.add("No stack trace" );
        }
        else{
            for(int i = 0; i<trace.length; i++){
                out.add(String.format("%s", trace[i]));
            }
        }
        return out;
    }
}
