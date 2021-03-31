package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;

public class TaskNegate<T> implements IGTreeTask<T> {
    private final char negateSymbol;

    public TaskNegate(char negateSymbol) {
        this.negateSymbol = negateSymbol;
    }

    @Override
    public boolean doTask(IGTreeNode<T> node) {
        String identifier = node.identifier();
        boolean negated = false;
        for(int i = 0; i < identifier.length(); i++){
            if(identifier.charAt(i) == negateSymbol){
                negated = !negated;
            }
            else{
                if(i == 0){
                    return false;
                }
                else if(i == identifier.length() - 1){
                    // TODO set error
                    System.out.println("negate error");
                    return false;
                }
                else {
                    node.setIdentifier(identifier.substring(i));
                    node.setNegated(negated);
                    return true;
                }
            }
        }
        return false;
    }
}
