package utilfile;

import err.ERR_TYPE;

import java.io.*;
import java.util.Properties;

public class UtilFileProps {
    private static UtilFileProps instance;

    public static UtilFileProps initInstance(){
        return (instance == null)? (instance = new UtilFileProps ()): instance;
    }

    private UtilFileProps(){}

    public ERR_TYPE propsFromFile(String filePath, Properties properties) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
            return ERR_TYPE.NONE;
        } catch (IOException e) {
            return ERR_TYPE.FILE_ERROR;
        }
    }

    public ERR_TYPE propsToFile(String filePath, Properties properties, String comment){
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            properties.store(outputStream, comment);
            return ERR_TYPE.NONE;
        } catch (IOException e) {
            return ERR_TYPE.FILE_ERROR;
        }
    }
}
