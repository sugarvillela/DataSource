package langdef.util;

import err.ERR_TYPE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;

import static langdef.STRUCT_LIST_TYPE.LIST_NUMBER;
import static langdef.STRUCT_LIST_TYPE.LIST_STRING;

class ListTypeSearchTest {
    int count;

    @BeforeEach
    void setUp() {
        count = 0;
        LIST_STRING.getListTree().clear();
    }

    private IReadNode getMockPayload(){
        int row = count;
        String text = "readNode_" + count;
        count ++;
        return ReadNode.builder().source("source").row(row).col(row).text(text).build();
    }

    private void mockListType(){
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING");
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING.TEXT");
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING.TEXT.IN");
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING.TEXT.ORIG");
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING.TEXT.SUB.A");
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING.TEXT.SUB.B");
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING.NAMES");
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING.NAMES.FIRST");
        LIST_STRING.getListTree().put(getMockPayload(), "LIST_STRING.NAMES.LAST");
        LIST_NUMBER.getListTree().put(getMockPayload(), "LIST_NUMBER");
        LIST_NUMBER.getListTree().put(getMockPayload(), "LIST_NUMBER.VALUE");
        LIST_NUMBER.getListTree().put(getMockPayload(), "LIST_NUMBER.VALUE.17");
    }

    @Test
    void givenPopulatedLists_findByPartialPath() {
        mockListType();
        String expected, actual;

        expected = "LIST_STRING.TEXT.IN|LIST_STRING.TEXT.ORIG|LIST_STRING.NAMES.FIRST|LIST_STRING.NAMES.LAST|LIST_NUMBER.VALUE.17";
        actual = String.join("|", Glob.LIST_TYPE_SEARCH.getAllPaths());
        Assertions.assertEquals(expected, actual);

        // partial path
        Assertions.assertEquals(ERR_TYPE.NONE, Glob.LIST_TYPE_SEARCH.pathFromPartialPath("FIRST"));
        expected = "LIST_STRING|NAMES|FIRST";
        actual = String.join("|", Glob.LIST_TYPE_SEARCH.pathFound());
        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals(ERR_TYPE.NONE, Glob.LIST_TYPE_SEARCH.pathFromPartialPath("17"));
        expected = "LIST_NUMBER|VALUE|17";
        actual = String.join("|", Glob.LIST_TYPE_SEARCH.pathFound());
        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals(ERR_TYPE.NONE, Glob.LIST_TYPE_SEARCH.pathFromPartialPath("VALUE"));
        expected = "LIST_NUMBER|VALUE";
        actual = String.join("|", Glob.LIST_TYPE_SEARCH.pathFound());
        Assertions.assertEquals(expected, actual);

        // is leaf
        Assertions.assertTrue(Glob.LIST_TYPE_SEARCH.isPathToLeaf("17"));
        Assertions.assertFalse(Glob.LIST_TYPE_SEARCH.isPathToLeaf("VALUE"));

        // node find
        Assertions.assertEquals(ERR_TYPE.NONE, Glob.LIST_TYPE_SEARCH.treeNodeFromPartialPath("FIRST"));
//        expected = "2,1,0,-,-,FIRST,readnode.impl.ReadNode@11c20519";//
//        actual = Glob.LIST_TYPE_SEARCH.treeNodeFound().csvString();
//        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals(ERR_TYPE.INVALID_PATH, Glob.LIST_TYPE_SEARCH.treeNodeFromPartialPath("binky"));
    }
}