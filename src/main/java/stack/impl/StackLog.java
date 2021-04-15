package stack.impl;

import langdefalgo.iface.LANG_STRUCT;
import stack.iface.IStackLog;

import java.util.ArrayList;

public class StackLog implements IStackLog {
    private final ArrayList<ArrayList<LANG_STRUCT>> stackLog;

    public StackLog() {
        stackLog = new ArrayList<>();
    }

    @Override
    public void addIteration(ArrayList<LANG_STRUCT> iteration) {
        stackLog.add(iteration);
    }

    @Override
    public ArrayList<LANG_STRUCT> lastIteration() {
        return (stackLog.isEmpty())? null : stackLog.get(stackLog.size() - 1);
    }

    @Override
    public ArrayList<LANG_STRUCT> lastIteration(int stackLevel) {
        final int MAX_LOOK_BACK = 2;
        final int end = Math.max(0, stackLog.size() - 1 - MAX_LOOK_BACK);
        for(int i = stackLog.size() - 1; i > end; i--){
            ArrayList<LANG_STRUCT> curr = stackLog.get(i);
            if(curr.size() > stackLevel){
                return curr;
            }
        }
        return null;
    }

    @Override
    public LANG_STRUCT lastIterationItem(int stackLevel) {
        //System.out.println(this.reportString());
        ArrayList<LANG_STRUCT> lastIteration = lastIteration(stackLevel);
        return (lastIteration == null)? null : lastIteration.get(stackLevel);
    }

    @Override
    public ArrayList<ArrayList<LANG_STRUCT>> allIterations() {
        return stackLog;
    }

    @Override
    public ArrayList<String> reportArray() {
        ArrayList<String> currRow = new ArrayList<>(), rows = new ArrayList<>();
        for( ArrayList<LANG_STRUCT> iterationItems : stackLog){
            if(!iterationItems.isEmpty()){
                for(LANG_STRUCT item : iterationItems){
                    currRow.add(item.toString());
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
