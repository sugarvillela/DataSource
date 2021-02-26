package datasource.util;

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
            System.out.println(e.getMessage());// TODO handle exception
        }
        return list;// returns empty list on fail
    }
}
