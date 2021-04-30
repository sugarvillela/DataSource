package langdefsubalgo.impl;

import langdef.CMD;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import textevent.impl.TextEvent;

import java.util.List;

import static langdef.STRUCT_KEYWORD.RX;
import static langdef.STRUCT_LIST_TYPE.LIST_NUMBER;
import static langdef.STRUCT_LIST_TYPE.LIST_STRING;

public class FxTest {
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

    private IReadNode getMockFxPayload(String text){
        int row = count;
        count ++;
        return ReadNode.builder().source("source").row(row).col(row).text(text).textEvent(new TextEvent(RX, CMD.PUSH)).build();
    }

    @Test
    void test(){
        String text;

        text = "TEXT.IN.LEN().RANGE(1:3)=TRUE";
        IReadNode mockPayload = getMockFxPayload(text);
        Glob.RUN_STATE.initTest();
        Glob.RUN_STATE.setCurrNode(mockPayload);
        mockListType();
    }
}
