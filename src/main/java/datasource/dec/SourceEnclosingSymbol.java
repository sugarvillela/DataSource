package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import static langdef.LangConstants.ITEM_OPEN;
import static langdef.LangConstants.ITEM_CLOSE;

public class SourceEnclosingSymbol extends DecoratorBase{
    private final String spaceOpen, spaceClose;
    private final Queue<IReadNode> queue;
    IReadNode nextNode, currNode;

    public SourceEnclosingSymbol(IDataSource dataSource) {
        super(dataSource);
        spaceOpen = String.format(" %s ", ITEM_OPEN);
        spaceClose = String.format(" %s ", ITEM_CLOSE);
        queue = new ArrayDeque<>();
        this.next();
    }

    @Override
    public boolean hasNext() {
        return nextNode != null || !queue.isEmpty();
    }

    @Override
    public IReadNode next() {
        currNode = nextNode;
        if(currNode != null){
            String text = currNode.text();
            String changeString = text.replace(ITEM_OPEN, spaceOpen).replace(ITEM_CLOSE, spaceClose);
            if(text.length() < changeString.length()){
                ArrayList<String> tokens = Glob.TOKENIZER.setText(changeString).parse().toList();
                for(String token : tokens){
                    IReadNode newNode = ReadNode.builder().copy(currNode).text(token).build();
                    queue.add(newNode);
                }
            }
            else{
                queue.add(currNode);
            }
        }
        nextNode = dataSource.next();

        return (queue.isEmpty())? currNode : queue.remove();
    }
}
