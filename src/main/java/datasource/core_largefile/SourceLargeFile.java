package datasource.core_largefile;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static datasource.LocalStatics.FILE_NAME_UTIL;

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
            // TODO handle exception
            scanner = new Scanner("");
            hasData = false;
        }
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
        throw new IllegalStateException("Peek not implemented; use a peek decorator");
    }

    @Override
    public boolean hasPeekAhead() {
        throw new IllegalStateException("Peek not implemented; use a peek decorator");
    }

    @Override
    public IReadNode peekBack() {
        throw new IllegalStateException("Peek not implemented; use a peek decorator");
    }

    @Override
    public IReadNode peekAhead() {
        throw new IllegalStateException("Peek not implemented; use a peek decorator");
    }

}
