package langdefalgo.impl;

import langdefalgo.iface.IAlgoStrategy;
import langdefalgo.strategy.PushPopStrategyGroup;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

import static langdef.STRUCT_KEYWORD.*;
import static langdef.STRUCT_LOOKUP.*;
import static langdef.STRUCT_NON_KEYWORD.*;
import static langdef.STRUCT_SYMBOL.*;
import static langdefalgo.strategy.PushPopStrategyGroup.FOLLOW_RULE;

public class AlgoImplGroupStep2 {
    private IAlgoStrategy[] pushes(PushPopStrategyGroup.PushStrategy... pushes){
        return pushes;
    }

    public void initAlgos(){
        RX.initAlgo(new CopyAll(), null, null);
        FX.initAlgo(new CopyAll(), pushes(FOLLOW_RULE), null);
        SCOPE.initAlgo(new CopyAll(), null, null);
        IF.initAlgo(new CopyAll(), null, null);
        ELSE.initAlgo(new CopyAll(), pushes(FOLLOW_RULE), null);

        // Struct lookup
        ID_ACCESS.initAlgo(new CopyAll(), null, null);

        // Struct non-keyword
        LANG_T.initAlgo(new CopyAll(), null, null);
        SCOPE_TEST.initAlgo(new CopyAll(), null, null);
        SCOPE_TEST_ITEM.initAlgo(new CopyAll(), null, null);
        RX_WORD.initAlgo(new CopyAll(), null, null);
        FX_WORD.initAlgo(new CopyAll(), null, null);

        // Struct symbol
        LANG_S.initAlgo(new CopyAll(), null, null);
        LANG_T_INSERT.initAlgo(new CopyAll(), null, null);
        A_FX.initAlgo(new CopyAll(), pushes(FOLLOW_RULE), null);
    }

    public static abstract class AlgoBaseStep2 extends AlgoBase {
        @Override
        public void onPush(IStackPayload stackTop) {
            if(this.pushes != null){
                super.onPush(stackTop);
            }
            Glob.DATA_SINK.put();
        }

        @Override
        public void onPop(IStackPayload stackTop) {
            Glob.DATA_SINK.put();
        }
    }

    // placeholder for enum handled by another algo, or completed on push
    private static class CopyAll extends AlgoBaseStep2 {
        @Override
        public boolean doCoreTask(IStackPayload stackTop) {
            Glob.DATA_SINK.put();
            return true;
        }
    }
}
