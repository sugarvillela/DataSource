package datasource.dec;

import datasource.TestUtil;
import datasource.dec_fluid.SourceFluid;
import datasource.iface.IDataSource;
import datasource.core.SourceArray;
import datasource.core.SourceFile;
import datasource.dec_tok.SourceTok;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import visitor.impl.EventProviderFluid;

import java.io.File;
import java.util.ArrayList;

import static datasource.LocalStatics.FILE_NAME_UTIL;
import static textpattern.TEXT_PATTERN.INSERT;

class DecoratorTest {
    String path;

    @BeforeEach
    void setUp() {
        path = "src"+ File.separator+"test"+File.separator+"resources";
    }

    @Test
    void nonEmptyShouldSkipEmptyLines() {
        IDataSource dataSource = new SourceNonEmpty(
                new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("testwspaces.txt"))
        );
        String actual = TestUtil.iterateAndJoin(dataSource);
        String expected =
                "testwspaces.txt,0,0,0,1,1,1,zero one two three,-|" +
                        "testwspaces.txt,1,0,0,1,1,1,four five six,-|" +
                        "testwspaces.txt,3,0,0,1,1,1,seven,-|" +
                        "testwspaces.txt,4,0,0,1,1,1,eight nine ten eleven twelve,-|" +
                        "testwspaces.txt,6,0,0,1,1,0,thirteen fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void nonEmptyShouldReturnNullOnOverrun() {
        IDataSource dataSource = new SourceNonEmpty(
                new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("testwspaces.txt"))
        );
        String actual = TestUtil.overrunAndJoin(dataSource, 10);
        String expected =
                "testwspaces.txt,0,0,0,1,1,1,zero one two three,-|" +
                        "testwspaces.txt,1,0,0,1,1,1,four five six,-|" +
                        "testwspaces.txt,3,0,0,1,1,1,seven,-|" +
                        "testwspaces.txt,4,0,0,1,1,1,eight nine ten eleven twelve,-|" +
                        "testwspaces.txt,6,0,0,1,1,0,thirteen fourteen,-|" +
                        "null|" +
                        "null|" +
                        "null|" +
                        "null|" +
                        "null";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void nonEmptyShouldSkipEmptyLinesAtEnd() {
        IDataSource dataSource = new SourceNonEmpty(
                new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("testwspaces2.txt"))
        );
        String actual = TestUtil.iterateAndJoin(dataSource);
        String expected =
                "testwspaces2.txt,0,0,0,1,1,1,zero one two three,-|" +
                        "testwspaces2.txt,1,0,0,1,1,1,four five six,-|" +
                        "testwspaces2.txt,3,0,0,1,1,1,seven,-|" +
                        "testwspaces2.txt,4,0,0,1,1,1,eight nine ten eleven twelve,-|" +
                        "testwspaces2.txt,6,0,0,1,1,1,thirteen fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void peekNext_sameAsNonPeek(){
        IDataSource dataSource = new SourcePeek(
                new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("test.txt"))
        );
        String actual = TestUtil.iterateAndJoin(dataSource);
        String expected = "test.txt,0,0,0,1,1,1,zero one two three,-|" +
                "test.txt,1,0,0,1,1,1,four five six,-|" +
                "test.txt,2,0,0,1,1,1,seven,-|" +
                "test.txt,3,0,0,1,1,1,eight nine ten eleven twelve,-|" +
                "test.txt,4,0,0,1,1,0,thirteen fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void peekShouldReturnNullOnOverrun() {
        IDataSource dataSource = new SourcePeek(
                new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("test.txt"))
        );
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
    void peekBack_shouldIncludeAll(){
        IDataSource dataSource = new SourcePeek(
                new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("test.txt"))
        );
        ArrayList<String> data = new ArrayList<>();
        int i = 0;
        while(dataSource.hasNext()){
            if(dataSource.hasPeekBack()){
                data.add(dataSource.peekBack().text());
            }
            dataSource.next();
            if(10 < i++){
                System.out.println("overrun!!!");
                break;
            }
        }
        if(dataSource.hasPeekBack()){
            data.add(dataSource.peekBack().text());
        }
        String actual = String.join("|", data);
        String expected = "zero one two three|four five six|seven|eight nine ten eleven twelve|thirteen fourteen";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void peekAhead_shouldSkipFirst(){
        IDataSource dataSource = new SourcePeek(
                new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("test.txt"))
        );
        ArrayList<String> data = new ArrayList<>();
        int i = 0;
        if(dataSource.hasPeekAhead()){
            data.add(dataSource.peekAhead().text());
        }
        while(dataSource.hasNext()){
            dataSource.next();
            if(dataSource.hasPeekAhead()){
                data.add(dataSource.peekAhead ().text());
            }
            if(10 < i++){
                System.out.println("overrun!!!");
                break;
            }
        }
        String actual = String.join("|", data);
        String expected = "four five six|seven|eight nine ten eleven twelve|thirteen fourteen";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void tokShouldReturnNullOnOverrun() {
        IDataSource dataSource = new SourceTok(
                new SourceFile(FILE_NAME_UTIL.mergeDefaultPath("test.txt"))
        );
        String actual = TestUtil.overrunAndJoin(dataSource, 20);
        String expected =
                "test.txt,0,0,0,1,0,1,zero,-|" +
                        "test.txt,0,1,0,1,0,1,one,-|" +
                        "test.txt,0,2,0,1,0,1,two,-|" +
                        "test.txt,0,3,0,1,1,1,three,-|" +
                        "test.txt,1,0,0,1,0,1,four,-|" +
                        "test.txt,1,1,0,1,0,1,five,-|" +
                        "test.txt,1,2,0,1,1,1,six,-|" +
                        "test.txt,2,0,0,1,1,1,seven,-|" +
                        "test.txt,3,0,0,1,0,1,eight,-|" +
                        "test.txt,3,1,0,1,0,1,nine,-|" +
                        "test.txt,3,2,0,1,0,1,ten,-|" +
                        "test.txt,3,3,0,1,0,1,eleven,-|" +
                        "test.txt,3,4,0,1,1,1,twelve,-|" +
                        "test.txt,4,0,0,1,0,0,thirteen,-|" +//behavior is correct; last token has endLine and !hasNext
                        "test.txt,4,1,0,1,1,0,fourteen,-|" +// << here
                        "null|" +
                        "null|" +
                        "null|" +
                        "null|" +
                        "null";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void tokShouldTokenizeAll(){
        String[] array = new String[]{
                "zero one two three",
                "four five six",
                "seven",
                "eight nine ten eleven twelve",
                "thirteen fourteen"
        };

        IDataSource dataSource = new SourceTok(
                new SourceArray(array)
        );
        String actual = TestUtil.iterateAndJoin(dataSource, 20);
        String expected =
                "array@0,0,0,0,1,0,1,zero,-|" +
                        "array@0,0,1,0,1,0,1,one,-|" +
                        "array@0,0,2,0,1,0,1,two,-|" +
                        "array@0,0,3,0,1,1,1,three,-|" +
                        "array@0,1,0,0,1,0,1,four,-|" +
                        "array@0,1,1,0,1,0,1,five,-|" +
                        "array@0,1,2,0,1,1,1,six,-|" +
                        "array@0,2,0,0,1,1,1,seven,-|" +
                        "array@0,3,0,0,1,0,1,eight,-|" +
                        "array@0,3,1,0,1,0,1,nine,-|" +
                        "array@0,3,2,0,1,0,1,ten,-|" +
                        "array@0,3,3,0,1,0,1,eleven,-|" +
                        "array@0,3,4,0,1,1,1,twelve,-|" +
                        "array@0,4,0,0,1,0,0,thirteen,-|" +
                        "array@0,4,1,0,1,1,0,fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void fluidShouldPushSourceAndPopWhenDone(){
        String[] array1 = new String[]{
                "zero one two three",
                "four five INSERT testAlt.txt six",
                "seven",
                "eight nine ten eleven twelve",
                "thirteen fourteen"
        };
        IDataSource dataSource = new SourceFluid(
            new PatternMatch(
                new SourceTok(
                    new SourceArray(array1)
                )
            ),
            new EventProviderFluid(INSERT)
        );

        String actual = TestUtil.iterateAndJoin(dataSource, 25);
        String expected =
                "array@0,0,0,0,1,0,1,zero,-|" +
                        "array@0,0,1,0,1,0,1,one,-|" +
                        "array@0,0,2,0,1,0,1,two,-|" +
                        "array@0,0,3,0,1,1,1,three,-|" +
                        "array@0,1,0,0,1,0,1,four,-|" +
                        "array@0,1,1,0,1,0,1,five,-|" +
                        "testAlt.txt,0,0,0,1,0,1,aaaa,-|" +
                        "testAlt.txt,0,1,0,1,1,1,bbbb,-|" +
                        "testAlt.txt,1,0,0,1,1,1,cccc,-|" +
                        "testAlt.txt,2,0,0,1,0,1,dddd,-|" +
                        "testAlt.txt,2,1,0,1,0,1,eeee,-|" +
                        "testAlt.txt,2,2,0,1,1,1,ffff,-|" +
                        "testAlt.txt,3,0,0,1,1,1,gggg,-|" +
                        "testAlt.txt,4,0,0,1,0,1,hhhh,-|" +
                        "testAlt.txt,4,1,0,1,1,1,iiii,-|" +// this is corrected by fluidSource
                        "array@0,1,4,0,1,1,1,six,-|" +
                        "array@0,2,0,0,1,1,1,seven,-|" +
                        "array@0,3,0,0,1,0,1,eight,-|" +
                        "array@0,3,1,0,1,0,1,nine,-|" +
                        "array@0,3,2,0,1,0,1,ten,-|" +
                        "array@0,3,3,0,1,0,1,eleven,-|" +
                        "array@0,3,4,0,1,1,1,twelve,-|" +
                        "array@0,4,0,0,1,0,1,thirteen,-|" +// this is corrected by fluidSource
                        "array@0,4,1,0,1,1,0,fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void fluidShouldReturnNullOnOverrun() {
        IDataSource dataSource = new SourceFluid(
                new SourceFile(FILE_NAME_UTIL.merge(path, "test.txt"))
        );
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
                        "null" ;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void patternMatchShouldSetEnums(){
        String[] array = new String[]{
                "zero one two three",
                "four five INSERT seven",
                "eight RX ten eleven twelve",
                "thirteen FX"
        };

        IDataSource dataSource = new PatternMatch(
            new SourceTok(
                    new SourceArray(array)
            )
        );
        String actual = TestUtil.iterateAndJoin(dataSource, 20);
        String expected =
                "array@0,0,0,0,1,0,1,zero,-|" +
                        "array@0,0,1,0,1,0,1,one,-|" +
                        "array@0,0,2,0,1,0,1,two,-|" +
                        "array@0,0,3,0,1,1,1,three,-|" +
                        "array@0,1,0,0,1,0,1,four,-|" +
                        "array@0,1,1,0,1,0,1,five,-|" +
                        "array@0,1,2,0,1,0,1,INSERT,INSERT|" +
                        "array@0,1,3,0,1,1,1,seven,-|" +
                        "array@0,2,0,0,1,0,1,eight,-|" +
                        "array@0,2,1,0,1,0,1,RX,RX|" +
                        "array@0,2,2,0,1,0,1,ten,-|" +
                        "array@0,2,3,0,1,0,1,eleven,-|" +
                        "array@0,2,4,0,1,1,1,twelve,-|" +
                        "array@0,3,0,0,1,0,0,thirteen,-|" +
                        "array@0,3,1,0,1,1,0,FX,FX";
        Assertions.assertEquals(expected, actual);
    }
}