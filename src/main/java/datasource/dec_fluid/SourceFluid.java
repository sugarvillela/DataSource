package datasource.dec_fluid;

import datasource.iface.IDataSource;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import runstate.Glob;

import java.util.Stack;

import static runstate.Glob.FACTORY_DATA_SOURCE;

public class SourceFluid implements IDataSource {
    protected final Stack<IDataSource> stack;
    protected final IDataSource initialSource;
    protected LANG_STRUCT langStruct;

    protected IReadNode prevNode, currNode;
    protected boolean state;

    public SourceFluid(IDataSource initialSource) {
        this.initialSource = initialSource;
        stack = new Stack<>();
        stack.push(initialSource);

        this.langStruct = Glob.ENUMS_BY_TYPE.sourceFluidLangStruct();// keep all hard-code langDef in lang def package
        this.state = false;

        this.next();
    }

    @Override
    public String sourceName() {
        return stack.peek().sourceName();
    }

    @Override
    public boolean hasData() {
        return initialSource != null && initialSource.hasData();
    }

    @Override
    public boolean hasNext() {
        return currNode != null;
    }

    protected void getOrPop(){
        while(!stack.isEmpty()){
            currNode = stack.peek().next();
            if(currNode == null){
                stack.pop();
            }
            else{
                break;
            }
        }
    }

    // stack.peek() source may think it is done, but there could be more on the stack
    protected void fixNodeDoneStatus(){
        if(prevNode != null){
            prevNode.setHasNext(!stack.isEmpty());
        }
    }

    @Override
    public IReadNode next() {
        prevNode = currNode;    // output one step behind
        getOrPop();             // get curr, pop if source finished
        fixNodeDoneStatus();    // make sure node.hasNext false only when stack is finished
        pushOnEvent();// push new file?
        return prevNode;
    }

    protected void pushOnEvent(){
        if(currNode != null){
            if(state){
                currNode.setActive(false);
                stack.push(FACTORY_DATA_SOURCE.getSourceSmall(
                    Glob.FILE_NAME_UTIL.mergeDefaultPath(currNode.text())
                    )
                );
                state = false;
            }
            else if(currNode.hasTextEvent() && this.langStruct == currNode.textEvent().langStruct()){
                currNode.setActive(false);
                state = true;
            }
        }
    }

    @Override
    public boolean hasPeekBack() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return false;
    }

    @Override
    public boolean hasPeekAhead() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return false;
    }

    @Override
    public IReadNode peekBack() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return null;
    }

    @Override
    public IReadNode peekAhead() {
        Glob.ERR_DEV.kill("Peek not implemented; use a peek decorator");
        return null;
    }
}
