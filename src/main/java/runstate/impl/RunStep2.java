package runstate.impl;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;
import runstate.iface.IRunStep;
import stack.iface.IStructStack;

public class RunStep2  extends RunStepBase {
    public RunStep2(IDataSource dataSource) {
        super(dataSource);
        System.out.println("RunStep2 construct");
    }

    @Override
    public void go() {
        System.out.println("RunStep2 go");
        this.prepareFirstTick();
        while(dataSource.hasNext()){
            System.out.println("a");
            currNode = dataSource.next();
            System.out.println("b");
            if(currNode == null){
                System.out.println("overrun");
                break;
            }
            else{
                System.out.println(currNode.csvString());
            }

            //structStack.top().getState().incTimeOnStack();
            //System.out.println(structStack.top().toString());
            //structStack.top().go();
        }

        System.out.println("\nfinished");
        System.out.println(structStack.getStackLog().reportString());
        System.out.println();
    }

    @Override
    public IReadNode getCurrNode() {
        return null;
    }

    @Override
    public IStructStack getStack() {
        return null;
    }
}
