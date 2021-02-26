package datasource.dec_fluid;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;
import visitor.iface.IEventProvider;
import visitor.iface.IEventReceiver;
import visitor.impl.EventProviderFluid;

import java.util.Stack;

public class SourceFluid implements IDataSource, IEventReceiver {
    private final IEventProvider eventProvider;
    private final Stack<IDataSource> stack;
    private final IDataSource initialSource;
    private IReadNode prevNode, currNode;

    public SourceFluid(IDataSource initialSource) {
        this.initialSource = initialSource;
        this.eventProvider = new EventProviderFluid();
        stack = new Stack<>();
        stack.push(initialSource);
        this.next();
    }
    public SourceFluid(IDataSource initialSource, IEventProvider eventProvider) {
        this.initialSource = initialSource;
        this.eventProvider = eventProvider;
        stack = new Stack<>();
        stack.push(initialSource);
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
        eventProvider.provide(this, currNode);// push new file?
        return prevNode;
    }

    @Override
    public void receive(IEventProvider provider, IDataSource dataSource) {
        stack.push(dataSource);
    }

    @Override
    public void receive(IEventProvider provider, boolean data) {}

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
