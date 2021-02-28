package datasource.dec_fluid;

import datasource.iface.IDataSource;
import langdef.iface.TEXT_PATTERN;
import readnode.iface.IReadNode;
import runstate.Glob;

import java.util.Stack;

import static runstate.Glob.FACTORY_DATA_SOURCE;

public class SourceFluid implements IDataSource {
    private final Stack<IDataSource> stack;
    private final IDataSource initialSource;
    private final TEXT_PATTERN textPattern;

    private IReadNode prevNode, currNode;
    private boolean state;

    public SourceFluid(IDataSource initialSource) {
        this.initialSource = initialSource;
        stack = new Stack<>();
        stack.push(initialSource);

        this.textPattern = Glob.ENUMS_BY_TYPE.sourceFluidTextPattern();// keep all hard-code langDef in lang def package
        this.state = false;

        this.next();
    }

    @Override
    public boolean hasData() {
        return initialSource != null && initialSource.hasData();
    }

    @Override
    public boolean hasNext() {
        return currNode != null;
    }

    private void getOrPop(){
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
    private void fixNodeDoneStatus(){
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

    private void pushOnEvent(){
        if(currNode != null){
            if(state){
                currNode.setActive(false);
                stack.push(FACTORY_DATA_SOURCE.getSourceSmall(
                    Glob.FILE_NAME_UTIL.mergeDefaultPath(currNode.text())
                    )
                );
                state = false;
            }
            else if(currNode.textEvent() != null && this.textPattern == currNode.textEvent().textPattern()){
                currNode.setActive(false);
                state = true;
            }
        }
    }

    @Override
    public boolean hasPeekBack() {
        throw new IllegalStateException("Peek not implemented; use a peek decorator");
    }

    @Override
    public boolean hasPeekAhead() {
        throw new IllegalStateException("Peek not implemented; use a peek decorator");
    }

    @Override
    public IReadNode peekBack() {
        throw new IllegalStateException("Peek not implemented; use a peek decorator");
    }

    @Override
    public IReadNode peekAhead() {
        throw new IllegalStateException("Peek not implemented; use a peek decorator");
    }
}
