package sourcestep.impl;

import datasink.iface.IDataSinkNode;
import datasource.dec.SourceTextEvent;
import langdefalgo.iface.LANG_STRUCT;
import org.junit.jupiter.api.*;
import readnode.iface.IReadNode;
import runstate.Glob;
import runstate.impl.RunState;
import textevent.iface.ITextEvent;
import textevent.impl.TextEvent;

import java.util.ArrayList;

import static langdef.CMD.POP;
import static langdef.CMD.PUSH;

class RunStepTest {
    static RunState runState;

    @BeforeAll
    static void beforeAll(){
        runState = RunState.initInstance();
        runState.initRunState();
    }
    @BeforeEach
    void beforeEach(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test2.rxfx");
        System.out.println(filePath);
        runState.setInFilePath(filePath);

    }

    @Test
    void go() {
        runState.initStep1();
        runState.go();

        runState.initStep2();
        runState.go();

        ArrayList<IDataSinkNode> allSinks = Glob.DATA_SINK.allSinks();
        for(IDataSinkNode sinkNode : allSinks){
            System.out.printf("\nSinkNode: %s %s \n", sinkNode.getParentEnum(), sinkNode.getIdentifier());
            for(IReadNode readNode : sinkNode.getNodes()){
                System.out.printf("%s \n", readNode.csvString());
            }
        }



//        System.out.println("STEP TWO");
//
//        IDataSource stepTwo =
//            new SourceActiveOnly(
//                new SourceAccess(
//                    Glob.DATA_SINK.getIdentifierOrErr(Glob.ENUMS_BY_TYPE.langRootEnum2().toString()).toDataSource()
//                )
//        );
//        while(stepTwo.hasNext()){
//            //stepTwo.next();
//            System.out.printf("%s \n", stepTwo.next().csvString());
//        }
//        ArrayList<IDataSinkNode> allSinks = Glob.DATA_SINK.allSinks();
//        for(IDataSinkNode sinkNode : allSinks){
//            System.out.printf("\nSinkNode: %s %s \n", sinkNode.getParentEnum(), sinkNode.getIdentifierOrErr());
//            for(IReadNode readNode : sinkNode.getNodes()){
//                System.out.printf("%s \n", readNode.csvString());
//            }
//        }
        //System.out.println(STRUCT_KEYWORD.CONSTANT.getSink(0).getLast().csvString());
//        String actual = TestUtil.iterateAndJoin(dataSource, 30);;
//        String expected = "array@0,0,0,0,1,0,1,FUN,FUN,PUSH,myFun|" +
//                "array@0,1,0,4,1,0,1,RX,RX,PUSH,myRx|" +
//                "array@0,1,2,0,1,0,1,two,-|" +
//                "array@0,1,3,0,1,1,1,three,-|" +
//                "array@0,2,0,0,1,1,0,END_FUN,FUN,POP,-";
//        Assertions.assertEquals(expected, actual);

    }

    @Test
    void testMuteUtil(){
        LANG_STRUCT langT = Glob.ENUMS_BY_TYPE.enumLangT();
        LANG_STRUCT langS = Glob.ENUMS_BY_TYPE.enumLangS();
        LANG_STRUCT ignoreMe = Glob.ENUMS_BY_TYPE.enumIdDefine();

        SourceTextEvent.ListenUtil listenUtil = new SourceTextEvent.ListenUtil();
        ITextEvent pushEventIgnore = new TextEvent(ignoreMe, PUSH);
        ITextEvent pushEventLangS = new TextEvent(langS, PUSH);
        ITextEvent pushEventLangT = new TextEvent(langT, PUSH);
        ITextEvent popEventLangS = new TextEvent(langS, POP);
        ITextEvent popEventLangT = new TextEvent(langT, POP);

        Assertions.assertFalse(listenUtil.shouldAddTextEvent(pushEventIgnore));// in lang T, no listen
        Assertions.assertFalse(listenUtil.shouldAddTextEvent(pushEventLangT));

        Assertions.assertTrue(listenUtil.shouldAddTextEvent(pushEventLangS));// to lang S, listen
        Assertions.assertTrue(listenUtil.shouldAddTextEvent(pushEventIgnore));

        Assertions.assertTrue(listenUtil.shouldAddTextEvent(pushEventLangT));// to lang T, no listen
        Assertions.assertFalse(listenUtil.shouldAddTextEvent(pushEventIgnore));

        Assertions.assertTrue(listenUtil.shouldAddTextEvent(popEventLangT));// to lang S, listen
        Assertions.assertTrue(listenUtil.shouldAddTextEvent(pushEventIgnore));

        Assertions.assertTrue(listenUtil.shouldAddTextEvent(popEventLangS));// to lang T, no listen
        Assertions.assertFalse(listenUtil.shouldAddTextEvent(pushEventIgnore));

    }


}