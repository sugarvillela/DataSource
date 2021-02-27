package datasource.core_largefile;

import datasource.TestUtil;
import datasource.iface.IDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static runstate.Glob.FILE_NAME_UTIL;

class SourceLargeFileTest {
    String path;

    @BeforeEach
    void setUp() {
        path = "src"+ File.separator+"test"+File.separator+"resources";
    }

    @Test
    void largeFileShouldIterate() {
        IDataSource dataSource = new SourceLargeFile(FILE_NAME_UTIL.merge(path, "test.txt"));
        String actual = TestUtil.iterateAndJoin(dataSource);
        String expected = "test.txt,0,0,0,1,1,1,zero one two three,-|" +
                "test.txt,1,0,0,1,1,1,four five six,-|" +
                "test.txt,2,0,0,1,1,1,seven,-|" +
                "test.txt,3,0,0,1,1,1,eight nine ten eleven twelve,-|" +
                "test.txt,4,0,0,1,1,0,thirteen fourteen,-";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void largeFileShouldReturnNullOnOverrun() {
        IDataSource dataSource = new SourceLargeFile(FILE_NAME_UTIL.merge(path, "test.txt"));
        String actual = TestUtil.overrunAndJoin(dataSource, 10);
        String expected =
                "test.txt,0,0,0,1,1,1,zero one two three,-|" +
                        "test.txt,1,0,0,1,1,1,four five six,-|" +
                        "test.txt,2,0,0,1,1,1,seven,-|" +
                        "test.txt,3,0,0,1,1,1,eight nine ten eleven twelve,-|" +
                        "test.txt,4,0,0,1,1,0,thirteen fourteen,-|" +
                        "null|" +
                        "null|" +
                        "null|" +
                        "null|" +
                        "null";
        Assertions.assertEquals(expected, actual);
    }
}