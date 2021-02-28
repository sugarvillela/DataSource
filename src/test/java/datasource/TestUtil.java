package datasource;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

import java.util.ArrayList;

/** All tests do one of two things: iterate for predictable output or overrun in predictable ways.
 *  So the common action happens in the static functions below, with input and output in the tests as expected.
 *
 *  Note: DataSourceBase, line 18 provides a unique identifier for list, array implementations
 *  This can break tests because the identifier is assigned sequentially (thus coupling it with other tests).
 *  Comment out the ++ and the tests will work
 */
public abstract class TestUtil {
    public static final boolean display = true;
    public static final boolean displayForCopy = false;

    public static String iterateAndJoin(IDataSource dataSource){
        return iterateAndJoin(dataSource, 20);
    }

    public static String iterateAndJoin(IDataSource dataSource, int overRun){
        if(display || displayForCopy){
            System.out.println("=============================");
        }
        ArrayList<String> data = new ArrayList<>();
        int i = 0;
        while(dataSource.hasNext()){

            IReadNode node = dataSource.next();
            String text = (node == null)? "null" : node.csvString();
            data.add(text);
            if(display){
                text = (node == null)? "null" : node.friendlyString();
                System.out.println("\"" + text + "|\" + ");
            }
            else if(displayForCopy){
                System.out.println("\"" + text + "|\" + ");
            }
            if(overRun < i++){
                System.out.println("overrun!!!");
                break;
            }
        }
        return String.join("|", data);
    }

    public static String overrunAndJoin(IDataSource dataSource, int overRun){
        ArrayList<String> data = new ArrayList<>();
        for(int i = 0; i< overRun; i++){
            IReadNode node = dataSource.next();
            String text = (node == null)? "null" : node.csvString();
            data.add(text);
            if(display){
                System.out.println(text);
            }
            else if(displayForCopy){
                System.out.println("\"" + text + "|\" + ");
            }
        }
        return String.join("|", data);
    }
}
