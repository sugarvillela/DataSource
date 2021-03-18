package utilfile;

import err.ERR_TYPE;
import runstate.Glob;

import java.io.*;
import java.util.ArrayList;

public class SmallFileDump {
    private static SmallFileDump instance;

    public static SmallFileDump initInstance(){
        return (instance == null)? (instance = new SmallFileDump()): instance;
    }

    private SmallFileDump(){}

    public ArrayList<String> toList(String filePath){
        ArrayList<String> list = new ArrayList<>();
        String line;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR);
        }
        return list;// returns empty list on fail
    }
}
