package sublang.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeParse;
import generictree.task.TaskDisp;
import generictree.task.TaskToList;
import langdef.CMD;
import langdef.LangConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import sublang.iface.ILogicTree;
import sublang.iface.ILogicTreeNode;
import textevent.impl.TextEvent;

import java.util.List;

import static langdef.STRUCT_KEYWORD.RX;
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
        LIST_STRING.getListTree().put("LIST_STRING", getMockPayload());
        LIST_STRING.getListTree().put("LIST_STRING.TEXT", getMockPayload());
        LIST_STRING.getListTree().put("LIST_STRING.TEXT.IN", getMockPayload());
        LIST_STRING.getListTree().put("LIST_STRING.TEXT.ORIG", getMockPayload());
        LIST_STRING.getListTree().put("LIST_STRING.TEXT.SUB.A", getMockPayload());
        LIST_STRING.getListTree().put("LIST_STRING.TEXT.SUB.B", getMockPayload());
        LIST_STRING.getListTree().put("LIST_STRING.NAMES", getMockPayload());
        LIST_STRING.getListTree().put("LIST_STRING.NAMES.Boris", getMockPayload());
        LIST_STRING.getListTree().put("LIST_STRING.NAMES.Ned", getMockPayload());
    }

    private void disp(List<String> list){
        System.out.println("====display====");
        for(String item : list){
            System.out.println("\"" + item + "|\" + ");
        }
        System.out.println("====end display====");
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
                "LIST_STRING.NAMES.Boris|" +
                "LIST_STRING.NAMES.Ned";
        Assertions.assertEquals(expected, actual);
    }

    private IReadNode getMockRxPayload(){
        int row = count;
        String text = "TEXT.IN.len().range(1,3)=TRUE|LIST_STRING.NAMES.has(Boris)=true";
        count ++;
        return ReadNode.builder().source("source").row(row).col(row).text(text).textEvent(new TextEvent(RX, CMD.PUSH)).build();
    }

    @Test
    void buildFromCurr(){
        Glob.RUN_STATE.initTest();
        Glob.RUN_STATE.setCurrNode(getMockRxPayload());
        ILogicTree logicTree = new LogicTree();
        //logicTree.build();

//        IGTreeParse<ILogicTreeNode> parse = logicTree.getTree().getParse();
//        logicTree.getTree().getParse().preOrder(logicTree.getTree().getRoot(), new TaskDisp<>());
    }
}
