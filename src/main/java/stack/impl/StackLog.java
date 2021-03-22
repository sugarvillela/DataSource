package stack.impl;

import stack.iface.IStackLog;
import stack.iface.IStackLogIterationItem;

import java.util.ArrayList;

public class StackLog implements IStackLog {
    private final ArrayList<ArrayList<IStackLogIterationItem>> stackLog;

    public StackLog() {
        stackLog = new ArrayList<>();
    }

    @Override
    public void addIteration(ArrayList<IStackLogIterationItem> iteration) {
        stackLog.add(iteration);
    }

    @Override
    public ArrayList<IStackLogIterationItem> lastIteration() {
        return (stackLog.isEmpty())? null : stackLog.get(stackLog.size() - 1);
    }

    @Override
    public ArrayList<IStackLogIterationItem> lastIteration(int stackLevel) {
        for(int i = stackLog.size() - 1; i >= 0; i--){
            ArrayList<IStackLogIterationItem> curr = stackLog.get(i);
            if(curr.size() > stackLevel){
                return curr;
            }
        }
        return null;
    }

    @Override
    public IStackLogIterationItem lastIterationItem(int stackLevel) {
        ArrayList<IStackLogIterationItem> lastIteration = lastIteration(stackLevel);
        return (lastIteration == null)? null : lastIteration.get(stackLevel);
    }

    @Override
    public ArrayList<ArrayList<IStackLogIterationItem>> allIterations() {
        return stackLog;
    }

    @Override
    public ArrayList<String> reportArray() {
        ArrayList<String> currRow = new ArrayList<>(), rows = new ArrayList<>();
        for( ArrayList<IStackLogIterationItem> iterationItems : stackLog){
            if(!iterationItems.isEmpty()){
                for(IStackLogIterationItem item : iterationItems){
                    currRow.add(item.csvString());
                }
                rows.add(String.join(",", currRow));
                currRow.clear();
            }
        }
        return rows;
    }

    @Override
    public String reportString() {
        return String.join("\n", reportArray());
    }

    @Override
    public String csvString() {
        return String.join("|", reportArray());
    }
}
