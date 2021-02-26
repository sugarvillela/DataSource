package datasource.util;

import java.io.File;

public class FileNameUtil {
    public static final String DEFAULT_PATH = "src"+ File.separator+"test"+File.separator+"resources";

    private static FileNameUtil instance;

    public static FileNameUtil initInstance(){
        return (instance == null)? (instance = new FileNameUtil()): instance;
    }

    private FileNameUtil(){}

    private String extension;

    public FileNameUtil setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String fixFileName(String fileName){
        return (fileName.endsWith(extension))?
                fileName : fileName + extension;
    }

    public String merge(String path, String fileName){
        return path + File.separator + fileName;
    }

    public String mergeDefaultPath(String fileName){
        return DEFAULT_PATH + File.separator + fileName;
    }

    public String fixAndMerge(String path, String fileName){
        return path + File.separator + this.fixFileName(fileName);
    }

    public String getFileNameFromPath(String path){
        int index = path.lastIndexOf(File.separator);
        return (index == -1)? path : path.substring(index + 1);
    }
}
