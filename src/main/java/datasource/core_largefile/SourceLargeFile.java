package datasource.core_largefile;

import datasource.iface.IDataSource;
import err.ERR_TYPE;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static runstate.Glob.FILE_NAME_UTIL;

public class SourceLargeFile implements IDataSource {
    private final String filePath, stringIdentifier;
    private Scanner scanner;
    private int row;
    private boolean hasData;

    public SourceLargeFile(String filePath) {
        this.filePath = filePath;
        this.stringIdentifier = FILE_NAME_UTIL.getFileNameFromPath(filePath);
        this.init();
    }
    private void init() {
        try{
            scanner = new Scanner(new File(filePath));
            hasData = scanner.hasNextLine();
            row = -1;
        }
        catch ( FileNotFoundException e ){
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR);
            scanner = new Scanner("");
            hasData = false;
        }
    }

    @Override
    public String sourceName() {
        return stringIdentifier;
    }

    @Override
    public boolean hasData() {
        return hasData;
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNextLine();
    }

    @Override
    public IReadNode next() {
        try{
            String nextStr = scanner.nextLine();
            row++;
            return new ReadNode(stringIdentifier, row, nextStr, scanner.hasNextLine());
        }
        catch ( NoSuchElementException | IllegalStateException e ){
            hasData = false;
            return null;
        }
    }

    @Override
    public boolean hasPeekBack() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return false;
    }

    @Override
    public boolean hasPeekAhead() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return false;
    }

    @Override
    public IReadNode peekBack() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return null;
    }

    @Override
    public IReadNode peekAhead() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return null;
    }

}
