package langdefsubalgo.implrx;

import generictree.iface.IGTreeNode;
import langdef.STRUCT_LIST_TYPE;
import langdefsub.PAR_TYPE;
import readnode.iface.IReadNode;
import runstate.Glob;

import java.util.Arrays;

import static langdefsub.FUN_TYPE.*;
import static langdefsub.PAR_TYPE.*;
import static langdefsub.PRIM_TYPE.*;

public abstract class RxFunImplGroup {
    public static class LiteralObject extends RxFunBase {
        private final String content;

        public LiteralObject(String[] content) {
            super(LIT, NULL, STRING);
            this.content = content[0];
            System.out.println("LiteralObject constructor");
        }
    }

    public static class LiteralNumber extends RxFunBase {
        private final int content;

        public LiteralNumber(int[] content) {
            super(LIT, NULL, NUMBER);
            this.content = content[0];
            System.out.println("LiteralNumber constructor");
        }
    }

    public static class LiteralBool extends RxFunBase {
        private final int content;

        public LiteralBool(int[] content) {
            super(LIT, NULL, BOOLEAN);
            this.content = content[0];
            System.out.println("LiteralBool constructor");
        }
    }

    public static class FunLen extends RxFunBase {
        public FunLen(PAR_TYPE parType, String[] content) {
            super(LEN, STRING, NUMBER, EMPTY_PAR);
            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            System.out.println("FunLen constructor");
        }
    }

    public static class FunRange extends RxFunBase {
        private final int[] content;

        public FunRange(PAR_TYPE parType, int[] content) {
            super(RANGE, NUMBER, BOOLEAN, NUM_RANGE);
            System.out.println("FunRange constructor: parType = " + parType);
            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            Glob.VALID_FUN_LIST.validateRange(this, content);
            this.content = content;
        }
        @Override
        public String description() {
            return String.format("%s -> %s(a:b) -> %s, params %s",
                    inType, funType, outType, Arrays.toString(paramTypes)
            );
        }
    }

    public static class FunPath extends RxFunBase {
        private final STRUCT_LIST_TYPE listType;
        private final String[] path;
        //private final IGTreeNode<IReadNode> treeNode;

        public FunPath(STRUCT_LIST_TYPE listType, String[] path, IGTreeNode<IReadNode> treeNode) {
            super(
                PATH,
                NULL,
                (treeNode.isLeaf())? listType.outType() : LIST,
                ID_PATH, ID_PAR
            );
            this.listType = listType;
            this.path = path;
            //this.treeNode = treeNode;
            System.out.println("FunPath constructor");
        }
    }
}
