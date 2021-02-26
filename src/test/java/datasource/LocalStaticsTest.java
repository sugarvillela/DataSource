package datasource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static datasource.LocalStatics.FILE_NAME_UTIL;
import static datasource.LocalStatics.SMALL_FILE_DUMP;

class LocalStaticsTest {
    String path;

    @BeforeEach
    void setUp() {
        path = "src"+ File.separator+"test"+File.separator+"resources";
    }

    @Test
    void merge() {
        String str = "test.txt";
        String expected = "src\\test\\resources\\test.txt";
        Assertions.assertEquals(expected, FILE_NAME_UTIL.merge(path, str));
    }

    @Test
    void fixAndMerge() {
        String str = "test.bmp";
        String expected = "src\\test\\resources\\test.bmp";
        Assertions.assertEquals(expected, FILE_NAME_UTIL.setExtension(".bmp").fixAndMerge(path, str));
    }

    @Test
    void fixFileName() {
        String str1 = "test.txt";
        String str2 = "test";
        String str3 = "test.jpg";

        String expected1 = "test.txt";
        String expected2 = "test.txt";
        String expected3 = "test.jpg.txt";

        Assertions.assertEquals(expected1, FILE_NAME_UTIL.setExtension(".txt").fixFileName(str1));
        Assertions.assertEquals(expected2, FILE_NAME_UTIL.fixFileName(str2));
        Assertions.assertEquals(expected3, FILE_NAME_UTIL.fixFileName(str3));
    }

    @Test
    void getFileNameFromPath() {
        String str = "src\\test\\resources\\test.txt";
        String expected = "test.txt";
        Assertions.assertEquals(expected, FILE_NAME_UTIL.getFileNameFromPath(str));
    }

    @Test
    void testResourcesPath(){//test path to resources folder
        {
            File file = new File(path);
            String absolutePath = file.getAbsolutePath();

            //System.out.println(absolutePath);
            Assertions.assertTrue(absolutePath.endsWith(path));
        }
        boolean good = true;
        try{
            Scanner scanner = new Scanner( new File(FILE_NAME_UTIL.merge(path, "test.txt")) );
            Assertions.assertTrue(scanner.hasNext());
        }catch(Exception e){
            System.out.println(e);
            good = false;
        }
        Assertions.assertTrue(good);
    }

    @Test
    void smallFileDump(){
        String filePath = FILE_NAME_UTIL.merge(path, "test.txt");
        //System.out.println(filePath);
        ArrayList<String> data = SMALL_FILE_DUMP.toList(FILE_NAME_UTIL.merge(path, "test.txt"));
        String actual = String.join("|", data);
        String expected = "zero one two three|four five six|seven|eight nine ten eleven twelve|thirteen fourteen";
        Assertions.assertEquals(expected, actual);
    }
}