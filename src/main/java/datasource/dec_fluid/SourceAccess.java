package datasource.dec_fluid;

import datasource.iface.IDataSource;
import langdef.CMD;
import readnode.iface.IReadNode;
import runstate.Glob;

public class SourceAccess extends SourceFluid {

    public SourceAccess(IDataSource initialSource) {
        super(initialSource);
        this.langStruct = Glob.ENUMS_BY_TYPE.enumIdAccess();// keep all hard-code langDef in lang def package
    }

    @Override
    public IReadNode next() {
        prevNode = currNode;    // output one step behind
        getOrPop();             // get curr, pop if source finished
        fixNodeDoneStatus();    // make sure node.hasNext false only when stack is finished

        pushOnEvent();// push new file?
        return prevNode;
    }

    @Override
    protected void pushOnEvent(){
//        String csv = (currNode == null)? "null" : currNode.csvString();
//        System.out.println(this.getClass().getSimpleName() + ": " + csv);

        if(
            currNode != null &&
            currNode.hasTextEvent() &&
            this.langStruct == currNode.textEvent().langStruct()
        ){
            if(currNode.textEvent().cmd() == CMD.PUSH){
                //System.out.println("inner: " + currNode.textEvent().substring());
                stack.push(Glob.DATA_SINK.getIdentifierOrErr(currNode).toDataSource());
            }
            currNode.setActive(false);
        }
    }
}
