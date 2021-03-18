package sourcestep.impl;

import datasink.iface.IDataSinkNode;
import langdef.STRUCT_KEYWORD;
import org.junit.jupiter.api.*;
import readnode.iface.IReadNode;
import runstate.Glob;
import runstate.impl.RunState;

import java.util.ArrayList;

class RunStepTest {
    static RunState runState;

    @BeforeAll
    static void beforeAll(){
        runState = RunState.initInstance();
        runState.initRunState();
    }
    @BeforeEach
    void beforeEach(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test1.rxfx");
        System.out.println(filePath);
        runState.setInFilePath(filePath);
        runState.initPreScan();
    }

    @Test
    void go() {
        runState.go();
        ArrayList<IDataSinkNode> allSinks = Glob.DATA_SINK.allSinks();
        for(IDataSinkNode sinkNode : allSinks){
            System.out.printf("\nSinkNode: %s %s \n", sinkNode.getParentEnum(), sinkNode.getIdentifier());
            for(IReadNode readNode : sinkNode.getNodes()){
                System.out.printf("%s \n", readNode.csvString());
            }
        }
        //System.out.println(STRUCT_KEYWORD.CONSTANT.getSink(0).getLast().csvString());
//        String actual = TestUtil.iterateAndJoin(dataSource, 30);;
//        String expected = "array@0,0,0,0,1,0,1,FUN,FUN,PUSH,myFun|" +
//                "array@0,1,0,4,1,0,1,RX,RX,PUSH,myRx|" +
//                "array@0,1,2,0,1,0,1,two,-|" +
//                "array@0,1,3,0,1,1,1,three,-|" +
//                "array@0,2,0,0,1,1,0,END_FUN,FUN,POP,-";
//        Assertions.assertEquals(expected, actual);
    }

}