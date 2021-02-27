package datasource.core;

import datasource.TestUtil;
import datasource.iface.IDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static runstate.Glob.FILE_NAME_UTIL;

class CoreTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void listShouldIterate() {
        ArrayList<String> list = new ArrayList<>();
        list.add("zero one two three");
        list.add("four five six");
        list.add("seven");
        list.add("eight nine ten eleven twelve");
        list.add("thirteen fourteen");
        IDataSource dataSource = new SourceList(list);

        String actual = TestUtil.iterateAndJoin(dataSource);
        String expected =
            "list@0,0,0,0,1,1,1,zero one two three,-|" +
            "list@0,1,0,0,1,1,1,four five six,-|" +
            "list@0,2,0,0,1,1,1,seven,-|" +
            "list@0,3,0,0,1,1,1,eight nine ten eleven twelve,-|" +
            "list@0,4,0,0,1,1,0,thirteen fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void arrayShouldIterate() {
        String[] array = {
            "zero one two three",
            "four five six",
            "seven",
            "eight nine ten eleven twelve",
            "thirteen fourteen"
        };
        IDataSource dataSource = new SourceArray(array);

        String actual = TestUtil.iterateAndJoin(dataSource);
        String expected =
            "array@0,0,0,0,1,1,1,zero one two three,-|" +
            "array@0,1,0,0,1,1,1,four five six,-|" +
            "array@0,2,0,0,1,1,1,seven,-|" +
            "array@0,3,0,0,1,1,1,eight nine ten eleven twelve,-|" +
            "array@0,4,0,0,1,1,0,thirteen fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void smallFileShouldIterate() {
        IDataSource dataSource = new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("test.txt"));
        String actual = TestUtil.iterateAndJoin(dataSource);
        String expected =
                "test.txt,0,0,0,1,1,1,zero one two three,-|" +
                        "test.txt,1,0,0,1,1,1,four five six,-|" +
                        "test.txt,2,0,0,1,1,1,seven,-|" +
                        "test.txt,3,0,0,1,1,1,eight nine ten eleven twelve,-|" +
                        "test.txt,4,0,0,1,1,0,thirteen fourteen,-";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void smallFileShouldReturnNullOnOverrun() {
        IDataSource dataSource = new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("test.txt"));
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

    @Test
    void arrayShouldReturnNullOnOverrun() {
        String[] array = {
            "zero one two three",
            "four five six",
            "seven",
            "eight nine ten eleven twelve",
            "thirteen fourteen"
        };

        IDataSource dataSource = new SourceArray(array);
        String actual = TestUtil.overrunAndJoin(dataSource, 10);
        String expected =
                "array@0,0,0,0,1,1,1,zero one two three,-|" +
                        "array@0,1,0,0,1,1,1,four five six,-|" +
                        "array@0,2,0,0,1,1,1,seven,-|" +
                        "array@0,3,0,0,1,1,1,eight nine ten eleven twelve,-|" +
                        "array@0,4,0,0,1,1,0,thirteen fourteen,-|" +
                        "null|" +
                        "null|" +
                        "null|" +
                        "null|" +
                        "null";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void sourceColShouldIterateWords() {
        String text = "    String  with too many      spaces";

        IDataSource dataSource = new SourceCol(text, "test.txt", 2, true);
        String actual = TestUtil.iterateAndJoin(dataSource);//TestUtil.iterateAndJoin
        String expected =
            "test.txt,2,0,4,1,0,1,String,-|" +
            "test.txt,2,1,1,1,0,1,with,-|" +
            "test.txt,2,2,0,1,0,1,too,-|" +
            "test.txt,2,3,0,1,0,1,many,-|" +
            "test.txt,2,4,5,1,1,1,spaces,-";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void sourceColShouldReturnNullOnOverrun() {
        String text = "    String  with too many      spaces";

        IDataSource dataSource = new SourceCol(text, "test.txt", 2, true);
        String actual = TestUtil.overrunAndJoin(dataSource, 10);//TestUtil.iterateAndJoin
        String expected =
            "test.txt,2,0,4,1,0,1,String,-|" +
            "test.txt,2,1,1,1,0,1,with,-|" +
            "test.txt,2,2,0,1,0,1,too,-|" +
            "test.txt,2,3,0,1,0,1,many,-|" +
            "test.txt,2,4,5,1,1,1,spaces,-|" +
            "null|" +
            "null|" +
            "null|" +
            "null|" +
            "null";
        Assertions.assertEquals(expected, actual);
    }
}