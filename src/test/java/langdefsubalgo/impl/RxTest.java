package langdefsubalgo.impl;

import generictree.iface.IGTree;
import langdef.CMD;
import langdef.LangConstants;
import langdefsubalgo.implrx.RxFunPattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import langdefsubalgo.iface.ILogicTree;
import runstate.Glob;
import textevent.impl.TextEvent;

import java.util.List;

import static langdef.STRUCT_KEYWORD.RX;
import static langdef.STRUCT_LIST_TYPE.LIST_NUMBER;
import static langdef.STRUCT_LIST_TYPE.LIST_STRING;

public class RxTest {
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

    private void disp(List<String> list){
        System.out.println("====display====");
        for(String item : list){
            System.out.println("\"" + item + "|\" + ");
        }
        System.out.println("====end display====");
    }

    private IReadNode getMockRxPayload(String text){
        int row = count;
        count ++;
        return ReadNode.builder().source("source").row(row).col(row).text(text).
                containerText(text).textEvent(new TextEvent(RX, CMD.PUSH)).build();
    }

    @Test
    void testListPaths(){
        mockListType();
        IGTree<IReadNode> listTree = LIST_STRING.getListTree();
        List<String> paths = listTree.getParse().getAllPaths(listTree.getRoot(), LangConstants.PATH_TREE_SEP);
        disp(paths);
        String actual = String.join("|", paths);
        String expected = "LIST_STRING.TEXT.IN|" +
                "LIST_STRING.TEXT.ORIG|" +
                "LIST_STRING.NAMES.FIRST|" +
                "LIST_STRING.NAMES.LAST";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void buildFromCurr(){
        String text;

        text = "TEXT.IN.LEN().RANGE(1:3)=TRUE";
        IReadNode mockPayload = getMockRxPayload(text);
        Glob.RUN_STATE.initTest();
        Glob.RUN_STATE.setCurrNode(mockPayload);
        mockListType();

        //System.out.println(String.join("|", Glob.LIST_TYPE_SEARCH.getAllPaths()));
        //System.out.println(Glob.LIST_TYPE_SEARCH.pathFromPartialPath("FIRST"));
        ILogicTree logicTree = new LogicTree(mockPayload);

//        IGTreeParse<IRxFunPattern> parse = logicTree.getTree().getParse();
//        logicTree.getTree().getParse().preOrder(logicTree.getTree().getRoot(), new TaskDisp<>());
    }
    @Test
    void rangeTest(){
        Glob.RUN_STATE.initTest();
        String text, actual, expected;
        IReadNode mockPayload;
        RxFunPattern funPattern;

        text = "TEXT.IN.LEN().RANGE(1:3)=TRUE{1:2}";
        expected = "strings=[TEXT.IN.LEN().RANGE(1:3)=TRUE]|numbers=[1, 2]";
        mockPayload = getMockRxPayload(text);
        Glob.RUN_STATE.setCurrNode(mockPayload);
        actual = new RxFunPattern(text).toString();
        Assertions.assertEquals(expected, actual);

        text = "TEXT.IN.LEN().RANGE(1:3)=TRUE*";
        expected = "strings=[TEXT.IN.LEN().RANGE(1:3)=TRUE]|numbers=[0, 1024]";
        mockPayload = getMockRxPayload(text);
        Glob.RUN_STATE.setCurrNode(mockPayload);
        actual = new RxFunPattern(text).toString();
        Assertions.assertEquals(expected, actual);

        text = "TEXT.IN.LEN().RANGE(1:3)=TRUE";
        expected = "strings=[TEXT.IN.LEN().RANGE(1:3)=TRUE]|numbers=[1, 1]";
        mockPayload = getMockRxPayload(text);
        Glob.RUN_STATE.setCurrNode(mockPayload);
        actual = new RxFunPattern(text).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenBadRange_quit(){
        Glob.RUN_STATE.initTest();
        String text, actual, expected;
        IReadNode mockPayload;
        RxFunPattern funPattern;

        text = "pattern{1:2}+";
        mockPayload = getMockRxPayload(text);
        Glob.RUN_STATE.setCurrNode(mockPayload);
        actual = new RxFunPattern(text).toString();
    }
    @Test
    void givenGoodCompareType_proceed(){
        String text = "TEXT.IN='foo'";
        IReadNode mockPayload = getMockRxPayload(text);
        Glob.RUN_STATE.initTest();
        Glob.RUN_STATE.setCurrNode(mockPayload);
        mockListType();
        new LogicTree(mockPayload);
    }
    @Test
    void givenBadCompareType_quit(){
        String text = "TEXT.IN.LEN().RANGE(1:3)='foo'";
        IReadNode mockPayload = getMockRxPayload(text);
        Glob.RUN_STATE.initTest();
        Glob.RUN_STATE.setCurrNode(mockPayload);
        mockListType();
        new LogicTree(mockPayload);
    }
    @Test
    void givenBadCompareLT_quit(){
        String text = "TEXT.IN.LEN().RANGE(1:3)<TRUE";
        IReadNode mockPayload = getMockRxPayload(text);
        Glob.RUN_STATE.initTest();
        Glob.RUN_STATE.setCurrNode(mockPayload);
        mockListType();
        new LogicTree(mockPayload);
    }
}
