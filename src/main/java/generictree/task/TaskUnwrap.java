package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;


public class TaskUnwrap<T> implements IGTreeTask<T> {
    private final char first, last;

    public TaskUnwrap(char first, char last) {
        this.first = first;
        this.last = last;
    }

    public boolean isWrapped(String identifier) {
        int level = 0, len = identifier.length();
        boolean outer = true;                   // Stays true if {a=b} or {{a=b}&{c=d}}
        for (int i = 0; i < len; i++) {
            if (identifier.charAt(i) == first) {
                level++;
            } else if (identifier.charAt(i) == last) {
                level--;
                if (level == 0 && i != len - 1) {// Finds {a}&{b}
                    outer = false;
                }
            }
        }
        if (level != 0) {// Finds {a}}
            // TODO set error
            System.out.println("wrap error");
        }
        return outer && identifier.charAt(0) == first && last == identifier.charAt(len - 1);
    }

    @Override
    public boolean doTask(IGTreeNode<T> node) {
        String identifier = node.identifier();
        if(!identifier.isEmpty() && this.isWrapped(identifier)){
            node.setIdentifier(identifier.substring(1, identifier.length() - 1));
            node.setWrapped(true);
            return true;
        }
        return false;
    }
}
