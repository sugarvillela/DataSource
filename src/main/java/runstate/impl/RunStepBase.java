package runstate.impl;

import datasource.iface.IDataSource;
import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import runstate.iface.IRunStep;
import stack.iface.IStructStack;
import stack.impl.StructStack;
import textevent.impl.TextEvent;

public abstract class RunStepBase implements IRunStep {
    protected final IDataSource dataSource;
    protected final IStructStack structStack;
    protected IReadNode currNode;

    public RunStepBase(IDataSource dataSource) {
        this.dataSource = dataSource;
        this.structStack = new StructStack();
    }

    protected void prepareFirstTick(){// manually create first node and stack push for target language
        //System.out.println("prepareFirstTick");
        LANG_STRUCT targetLanguageEnum = Glob.ENUMS_BY_TYPE.enumLangT();
        currNode = new ReadNode(dataSource.sourceName(), 0, "", dataSource.hasNext());
        currNode.setTextEvent(new TextEvent(targetLanguageEnum, CMD.PUSH));
        structStack.push(targetLanguageEnum.newStackPayload());
        //System.out.println("prepareFirstTick done");
    }

    @Override
    public void setCurrNode(IReadNode currNode) {// for testing
        this.currNode = currNode;
    }

    @Override
    public IReadNode getCurrNode() {// backNode always null except after goBack() and before go()
        return currNode;
    }

    @Override
    public IStructStack getStack() {
        return this.structStack;
    }
}
