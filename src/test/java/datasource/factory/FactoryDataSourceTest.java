package datasource.factory;

import datasource.TestUtil;
import datasource.iface.IDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static runstate.Glob.FACTORY_DATA_SOURCE;

class FactoryDataSourceTest {
    String path;

    @BeforeEach
    void setUp() {
        path = "src"+ File.separator+"test"+File.separator+"resources";
    }

    @Test
    void shouldTokenizeAll(){
        String[] array = new String[]{
                "zero one two three",
                "four five six",
                "seven",
                "eight nine ten eleven twelve",
                "thirteen fourteen"
        };

        IDataSource dataSource = FACTORY_DATA_SOURCE.getSourceSmall(
                array
        );
        String actual = TestUtil.iterateAndJoin(dataSource);
        String expected = "array@0,0,0,0,1,0,1,zero,-|" +
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
                "array@0,4,0,0,1,0,1,thirteen,-|" +
                "array@0,4,1,0,1,1,0,fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldIgnoreKeyWordsWhileChangingSources(){
        String[] array = new String[]{
                "zero one two",
                "three",
                "four five six",
                "beagle",
                "seven INSERT testAlt.txt",
                "eight nine bulldog ten",
                "eleven twelve thirteen fourteen"
        };
        IDataSource dataSource = FACTORY_DATA_SOURCE.getSource(
            array
        );
        String actual = TestUtil.iterateAndJoin(dataSource, 30);;
        String expected =
                "array@0,0,0,0,1,0,1,zero,-|" +
                        "array@0,0,1,0,1,0,1,one,-|" +
                        "array@0,0,2,0,1,1,1,two,-|" +
                        "array@0,1,0,0,1,1,1,three,-|" +
                        "array@0,2,0,0,1,0,1,four,-|" +
                        "array@0,2,1,0,1,0,1,five,-|" +
                        "array@0,2,2,0,1,1,1,six,-|" +
                        "array@0,3,0,0,1,1,1,beagle,-|" +
                        "array@0,4,0,0,1,0,1,seven,-|" +
                        "testAlt.txt,0,0,0,1,0,1,aaaa,-|" +
                        "testAlt.txt,0,1,0,1,1,1,bbbb,-|" +
                        "testAlt.txt,1,0,0,1,1,1,cccc,-|" +
                        "testAlt.txt,2,0,0,1,0,1,dddd,-|" +
                        "testAlt.txt,2,1,0,1,0,1,eeee,-|" +
                        "testAlt.txt,2,2,0,1,1,1,ffff,-|" +
                        "testAlt.txt,3,0,0,1,1,1,gggg,-|" +
                        "testAlt.txt,4,0,0,1,0,1,hhhh,-|" +
                        "testAlt.txt,4,1,0,1,1,1,iiii,-|" +
                        "array@0,5,0,0,1,0,1,eight,-|" +
                        "array@0,5,1,0,1,0,1,nine,-|" +
                        "array@0,5,2,0,1,0,1,LINE,-|" +
                        "array@0,5,3,0,1,1,1,ten,-|" +
                        "array@0,6,0,0,1,0,1,eleven,-|" +
                        "array@0,6,1,0,1,0,1,twelve,-|" +
                        "array@0,6,2,0,1,0,1,thirteen,-|" +
                        "array@0,6,3,0,1,1,0,fourteen,-";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void shouldSkipComments(){// separate symbol, connected symbol and last word
        String[] array = new String[]{
                "zero one two",
                "three",
                "four five six # THIS IS A COMMENT",
                "seven #THIS IS A CONNECTED COMMENT",
                "eight nine ten #LAST",
                "#SKIP THE WHOLE THING",
                "eleven twelve thirteen fourteen # LAST OF ALL"
        };
        IDataSource dataSource = FACTORY_DATA_SOURCE.getSource(
                array
        );
        String actual = TestUtil.iterateAndJoin(dataSource, 30);;
        String expected =
                "array@0,0,0,0,1,0,1,zero,-|" +
                        "array@0,0,1,0,1,0,1,one,-|" +
                        "array@0,0,2,0,1,1,1,two,-|" +
                        "array@0,1,0,0,1,1,1,three,-|" +
                        "array@0,2,0,0,1,0,1,four,-|" +
                        "array@0,2,1,0,1,0,1,five,-|" +
                        "array@0,2,2,0,1,1,1,six,-|" +
                        "array@0,3,0,0,1,1,1,seven,-|" +
                        "array@0,4,0,0,1,0,1,eight,-|" +
                        "array@0,4,1,0,1,0,1,nine,-|" +
                        "array@0,4,2,0,1,1,1,ten,-|" +
                        "array@0,6,0,0,1,0,1,eleven,-|" +
                        "array@0,6,1,0,1,0,1,twelve,-|" +
                        "array@0,6,2,0,1,0,1,thirteen,-|" +
                        "array@0,6,3,0,1,1,0,fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldSkipCommentsInPushedSource(){// separate symbol, connected symbol and last word
        String[] array1 = new String[]{
                "zero one two",
                "three",
                "four five six # THIS IS A COMMENT",
                "seven INSERT testAltwcomments.txt",
                "eight nine ten #LAST",
                "#SKIP THE WHOLE THING",
                "eleven twelve thirteen fourteen # LAST OF ALL"
        };
        IDataSource dataSource = FACTORY_DATA_SOURCE.getSource(
                array1
        );
        String actual = TestUtil.iterateAndJoin(dataSource, 30);;
        String expected =
                "array@0,0,0,0,1,0,1,zero,-|" +
                        "array@0,0,1,0,1,0,1,one,-|" +
                        "array@0,0,2,0,1,1,1,two,-|" +
                        "array@0,1,0,0,1,1,1,three,-|" +
                        "array@0,2,0,0,1,0,1,four,-|" +
                        "array@0,2,1,0,1,0,1,five,-|" +
                        "array@0,2,2,0,1,1,1,six,-|" +
                        "array@0,3,0,0,1,0,1,seven,-|" +
                        "testAltwcomments.txt,0,0,0,1,0,1,aaaa,-|" +
                        "testAltwcomments.txt,0,1,0,1,1,1,bbbb,-|" +
                        "testAltwcomments.txt,1,0,0,1,1,1,cccc,-|" +
                        "testAltwcomments.txt,2,0,0,1,0,1,dddd,-|" +
                        "testAltwcomments.txt,2,1,0,1,0,1,eeee,-|" +
                        "testAltwcomments.txt,2,2,0,1,1,1,ffff,-|" +
                        "testAltwcomments.txt,3,0,0,1,1,1,gggg,-|" +
                        "testAltwcomments.txt,4,0,0,1,0,1,hhhh,-|" +
                        "testAltwcomments.txt,4,1,0,1,1,1,iiii,-|" +
                        "array@0,4,0,0,1,0,1,eight,-|" +
                        "array@0,4,1,0,1,0,1,nine,-|" +
                        "array@0,4,2,0,1,1,1,ten,-|" +
                        "array@0,6,0,0,1,0,1,eleven,-|" +
                        "array@0,6,1,0,1,0,1,twelve,-|" +
                        "array@0,6,2,0,1,0,1,thirteen,-|" +
                        "array@0,6,3,0,1,1,0,fourteen,-";
        Assertions.assertEquals(expected, actual);
    }

//    @Test
//    void shouldSet(){
//        String[] array = new String[]{
//                "zero one two three",
//                "four five six",
//                "seven",
//                "eight nine ten eleven twelve",
//                "thirteen fourteen"
//        };
//
//        IDataSource dataSource = FACTORY_DATA_SOURCE.getSourceSmall(
//                array
//        );
//        String actual = TestUtil.iterateAndJoin(dataSource);
//        String expected = "array@0,0,0,0,1,0,1,zero,-|" +
//                "array@0,0,1,0,1,0,1,one,-|" +
//                "array@0,0,2,0,1,0,1,two,-|" +
//                "array@0,0,3,0,1,1,1,three,-|" +
//                "array@0,1,0,0,1,0,1,four,-|" +
//                "array@0,1,1,0,1,0,1,five,-|" +
//                "array@0,1,2,0,1,1,1,six,-|" +
//                "array@0,2,0,0,1,1,1,seven,-|" +
//                "array@0,3,0,0,1,0,1,eight,-|" +
//                "array@0,3,1,0,1,0,1,nine,-|" +
//                "array@0,3,2,0,1,0,1,ten,-|" +
//                "array@0,3,3,0,1,0,1,eleven,-|" +
//                "array@0,3,4,0,1,1,1,twelve,-|" +
//                "array@0,4,0,0,1,0,1,thirteen,-|" +
//                "array@0,4,1,0,1,1,0,fourteen,-";
//        Assertions.assertEquals(expected, actual);
//    }
}