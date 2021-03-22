package stack.iface;

import java.util.ArrayList;

public interface IStackLog {//2-d
    void addIteration(ArrayList<IStackLogIterationItem> iteration);
    ArrayList<IStackLogIterationItem> lastIteration();
    ArrayList<IStackLogIterationItem> lastIteration(int stackLevel);
    IStackLogIterationItem lastIterationItem(int stackLevel);
    ArrayList<ArrayList<IStackLogIterationItem>> allIterations();
    ArrayList<String> reportArray();
    String reportString();
    String csvString();
}
