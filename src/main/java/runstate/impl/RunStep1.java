package runstate.impl;

import attrib.types.RUNTIME_ATTRIB;
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

public class RunStep1 extends RunStepBase {

    public RunStep1(IDataSource dataSource) {
        super(dataSource);
    }


    private void finishLastTick(){
        IReadNode wordPopNode = ReadNode.builder().copy(currNode).textEvent(new TextEvent(Glob.ENUMS_BY_TYPE.enumLangT(), CMD.POP)).build();
        Glob.DATA_SINK.put(wordPopNode);
    }

    @Override
    public void go() {
        this.prepareFirstTick();
        while(dataSource.hasNext()){
            currNode = dataSource.next();
            if(currNode == null){
                System.out.println("overrun");
                break;
            }
            else{
                //System.out.println(currNode.csvString());
            }

            structStack.top().getState().incTimeOnStack();
            //System.out.println(structStack.top().toString());
            structStack.top().go();
        }
        //this.finishLastTick();

//        System.out.println("\nfinished");
//        System.out.println(structStack.getStackLog().reportString());
//        System.out.println();

//        RUNTIME_ATTRIB.props.display();
    }

}
