package langdefalgo.impl;

import stackpayload.iface.IStackPayload;

import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.*;

public class AlgoImplGroupStep2 {
    private static final int FIRST = 0, SECOND = 1;

    public void initAlgos(){

        // Struct keyword  // no sharing singletons: each algo needs a unique state
        ATTRIB.initAlgo(new NoCoreTask());
        CONSTANT.initAlgo(new NoCoreTask());
        RX.initAlgo(new NoCoreTask());
        FX.initAlgo(new NoCoreTask());
        FUN.initAlgo(new NoCoreTask());
        SCOPE.initAlgo(new NoCoreTask());
        IF.initAlgo(new NoCoreTask());
        ELSE.initAlgo(new NoCoreTask());
        INCLUDE.initAlgo(new NoCoreTask());

        // Struct lookup
        ID_DEFINE.initAlgo(new NoCoreTask());
        ID_ACCESS.initAlgo(new NoCoreTask());
        COMMENT.initAlgo(new NoCoreTask());

        // Struct non-keyword
        LANG_T.initAlgo(new NoCoreTask());
        SCOPE_TEST.initAlgo(new NoCoreTask());
        SCOPE_TEST_ITEM.initAlgo(new NoCoreTask());
        RX_WORD.initAlgo(new NoCoreTask());
        FX_WORD.initAlgo(new NoCoreTask());
        LANG_ROOT_1.initAlgo(new NoCoreTask());
        //LANG_ROOT_2.initAlgo(new NoCoreTask());

        // Struct symbol
        LANG_S.initAlgo(new NoCoreTask());
        LANG_T_INSERT.initAlgo(new NoCoreTask());
        A_FX.initAlgo(new NoCoreTask());
    }

    public static abstract class AlgoBaseStep2 extends AlgoBase {
        @Override
        public void onPush(IStackPayload stackTop) {
            //onPush_putPushNode();
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            onPop_putPopNode();
        }
    }

    // placeholder for enum handled by another algo, or completed on push
    public static class NoCoreTask extends AlgoBaseStep2 {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            return true;
        }
    }
}
