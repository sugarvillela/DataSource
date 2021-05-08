package langdefsubalgo.impl;

import generictree.iface.IGTreeNode;
import langdef.STRUCT_LIST_TYPE;
import langdefsub.PAR_TYPE;
import langdefsubalgo.iface.IFun;
import readnode.iface.IReadNode;
import runstate.Glob;

import java.util.Arrays;

import static langdefsub.FUN_TYPE.*;
import static langdefsub.PAR_TYPE.*;
import static langdefsub.PRIM_TYPE.*;

// types before and after: null means don't care; empty means disallow
public abstract class FunImplGroup {
    public static class LiteralObject extends FunBase {
        private final String content;

        public LiteralObject(IFun prev, String[] content) {
            super(prev);
            this.setFunType(LIT);
            this.setPrimTypeBefore(NULL);
            this.setPrimTypeAfter(STRING);
            this.setParamTypes(EMPTY_PAR);
            this.setFunTypesBefore();
            this.setParTypesBefore();

            this.content = content[0];
            System.out.println("LiteralObject constructor");

            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }
    }

    public static class LiteralNumber extends FunBase {
        private final int content;

        public LiteralNumber(IFun prev, int[] content) {
            super(prev);
            this.setFunType(LIT);
            this.setPrimTypeBefore(NULL);
            this.setPrimTypeAfter(NUMBER);
            this.setParamTypes(EMPTY_PAR);
            this.setFunTypesBefore();
            this.setParTypesBefore();

            this.content = content[0];
            System.out.println("LiteralNumber constructor");

            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }
    }

    public static class LiteralBool extends FunBase {
        private final int content;

        public LiteralBool(IFun prev, int[] content) {
            super(prev);
            this.setFunType(LIT);
            this.setPrimTypeBefore(NULL);
            this.setPrimTypeAfter(BOOLEAN);
            this.setParamTypes(EMPTY_PAR);
            this.setFunTypesBefore();
            this.setParTypesBefore();

            this.content = content[0];
            System.out.println("LiteralBool constructor");

            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }
    }

    public static class FunGetPath extends FunBase {// auto function for dot-separated paths in RX
        private final STRUCT_LIST_TYPE listType;
        private final String[] path;
        //private final IGTreeNode<IReadNode> treeNode;

        public FunGetPath(IFun prev, STRUCT_LIST_TYPE listType, String[] path, IGTreeNode<IReadNode> treeNode) {
            super(prev);
            this.setFunType(GET_PATH);
            this.setPrimTypeBefore(NULL);
            this.setPrimTypeAfter((treeNode.isLeaf())? listType.outType() : LIST);
            this.setParamTypes(ID_PATH, ID_PAR);
            this.setFunTypesBefore(GET_ACCESS);

            this.listType = listType;
            this.path = path;
            //this.treeNode = treeNode;
            System.out.println("FunGetPath constructor");

            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }

        @Override
        public String[] stringContent() {
            return this.path;
        }
    }

    // rx front end
    public static class FunLen extends FunBase {
        public FunLen(IFun prev, PAR_TYPE parType, String[] content) {
            super(prev);
            this.setFunType(LEN);
            this.setPrimTypeBefore(STRING);
            this.setPrimTypeAfter(NUMBER);
            this.setParamTypes(EMPTY_PAR);

            System.out.println("FunLen constructor");

            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }
    }

    public static class FunRange extends FunBase {
        private final int[] content;

        public FunRange(IFun prev, PAR_TYPE parType, int[] content) {
            super(prev);
            this.setFunType(RANGE);
            this.setPrimTypeBefore(NUMBER);
            this.setPrimTypeAfter(BOOLEAN);
            this.setParamTypes(NUM_RANGE);
            this.content = content;

            System.out.println("FunRange constructor: parType = " + parType);

            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            Glob.VALID_FUN_LIST.validateRangeLength(this, content);
            Glob.VALID_FUN_LIST.validateRange(this, content);

            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }
        @Override
        public String description() {
            return String.format("%s -> %s(a:b) -> %s, params: %s",
                    primTypeBefore, funType, primTypeAfter, Arrays.toString(paramTypes)
            );
        }
    }

    // fx flags
    public static class FunVote extends FunBase {
        public FunVote(IFun prev, PAR_TYPE parType) {
            super(prev);
            this.setFunType(VOTE);
            this.setParamTypes(EMPTY_PAR);

            this.setFunTypesBefore(GET_PATH);

            this.parTypeEnum = parType;
            System.out.println("FunVote constructor");

            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);

        }
    }

    public static class FunSet extends FunBase {
        public FunSet(IFun prev, PAR_TYPE parType) {
            super(prev);
            this.setFunType(SET);
            this.setPrimTypeAfter(NULL);
            this.setParamTypes(EMPTY_PAR);

            this.setFunTypesBefore(GET_PATH);

            this.parTypeEnum = parType;
            System.out.println("FunSet constructor");

            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);

        }
    }

    public static class FunDrop extends FunBase {
        public FunDrop(IFun prev, PAR_TYPE parType) {
            super(prev);
            this.setFunType(DROP);
            this.setPrimTypeBefore(PATH);
            this.setPrimTypeAfter(NULL);
            this.setParamTypes(EMPTY_PAR);

            this.setFunTypesBefore(GET_PATH);

            this.parTypeEnum = parType;
            System.out.println("FunDrop constructor");

            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }
    }

    // fx structural
    public static class FunSwap extends FunBase {
        public FunSwap(IFun prev, PAR_TYPE parType) {
            super(prev);
            this.setFunType(SWAP);
            this.setPrimTypeBefore(NULL);
            this.setPrimTypeAfter(NULL);
            this.setParamTypes(EMPTY_PAR);

            this.setFunTypesBefore(GET_ACCESS);
            this.setParTypesBefore(NUM_LIST);

            this.parTypeEnum = parType;
            System.out.println("FunSwap constructor");

            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }
    }

    public static class FunCon extends FunBase {

        public FunCon(IFun prev, PAR_TYPE parType) {
            super(prev);
            this.setFunType(CON);
            this.setPrimTypeBefore(NULL);
            this.setPrimTypeAfter(NULL);
            this.setParamTypes(EMPTY_PAR);

            this.setFunTypesBefore(GET_ACCESS);
            this.setParTypesBefore(NUM_PAR, NUM_LIST);

            this.parTypeEnum = parType;
            System.out.println("FunCon constructor");

            Glob.VALID_FUN_LIST.validateParamType(this, parType);
            Glob.VALID_FUN_LIST.validatePrimTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateFunTypeChain(prev, this);
            Glob.VALID_FUN_LIST.validateRangeBeforeInChain(prev, this);
        }
    }
                /*
    // fx structure
    CON,
    PUT,
    SWAP
            * */
}
