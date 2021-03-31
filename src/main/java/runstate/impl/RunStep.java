package runstate.impl;

import attrib.types.RUNTIME_ATTRIB;
import datasource.iface.IDataSource;
import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;

import readnode.impl.ReadNode;
import runstate.Glob;
import runstate.iface.IRunStep;
import stack.iface.IStackLog;
import stack.iface.IStructStack;
import stack.impl.StackLog;
import stack.impl.StructStack;
import stackpayload.iface.IStackPayload;
import textevent.impl.TextEventNode;

public class RunStep implements IRunStep {
    private final IDataSource dataSource;
    private final IStructStack structStack;
    private IReadNode currNode, backNode;

    public RunStep(IDataSource dataSource) {//IFluidTokenSource tokenSource
        this.dataSource = dataSource;
        this.structStack = new StructStack();
    }
    private void prepareFirstTick(){// manually create first node and stack push for target language
        LANG_STRUCT targetLanguageEnum = Glob.ENUMS_BY_TYPE.targetLangEnum();
        currNode = new ReadNode(dataSource.sourceName(), 0, "", dataSource.hasNext());
        currNode.setTextEvent(new TextEventNode(targetLanguageEnum, CMD.PUSH));
        structStack.push(targetLanguageEnum.newStackPayload());
    }
    private void finishLastTick(){
        IReadNode wordPopNode = ReadNode.builder().copy(currNode).textEvent(new TextEventNode(Glob.ENUMS_BY_TYPE.targetLangEnum(), CMD.POP)).build();
        Glob.DATA_SINK.put(wordPopNode);
        structStack.pop();
    }

    @Override
    public void go() {
        this.prepareFirstTick();
        int i = 0;
        while(dataSource.hasNext()){
            if(backNode == null){
                currNode = dataSource.next();
                //LANG_STRUCT langRootEnum2 = Glob.ENUMS_BY_TYPE.langRootEnum2();
                //Glob.DATA_SINK.getIdentifier(langRootEnum2.toString()).put(currNode);
            }
            else{
                currNode = backNode;
                //System.out.println("have backNode: " + backNode.csvString());
                backNode = null;
            }
            if(currNode == null){
                System.out.println("overrun");
                break;
            }
            else{
                System.out.println(currNode.csvString());
            }


            structStack.top().getState().incTimeOnStack();
            //System.out.println(structStack.top().toString());
            structStack.top().go();
        }
        //this.finishLastTick();

        System.out.println("\nfinished");
        System.out.println(structStack.getStackLog().reportString());
        System.out.println();

        RUNTIME_ATTRIB.props.display();
    }

    @Override
    public IReadNode getCurrNode() {// backNode always null except after goBack() and before go()
        return (backNode == null)? currNode : backNode;
    }

    @Override
    public void goBack(IReadNode backNode){
        //System.out.println("goBack: " + backNode.csvString());
        this.backNode = backNode;
    }

    @Override
    public IStructStack getStack() {
        return this.structStack;
    }

    @Override
    public void setCurrNode(IReadNode newNode) {
        currNode = newNode;
    }

    /*=====StructStack wrapper methods================================================================================*/

    @Override
    public void push(IStackPayload stackPayload) {
        structStack.push(stackPayload);
    }

    @Override
    public void pop() {
        structStack.pop();
    }

    @Override
    public void popMost() {
        structStack.popMost();
    }

    @Override
    public IStackPayload top() {
        return structStack.top();
    }

    @Override
    public int size() {
        return structStack.size();
    }

    @Override
    public IStackLog getStackLog() {
        return structStack.getStackLog();
    }
}
