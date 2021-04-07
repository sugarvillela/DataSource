package datasource.dec_tok;

import datasource.dec.DecoratorBase;
import datasource.iface.IDataSource;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import static langdef.LangConstants.ITEM_OPEN;
import static langdef.LangConstants.ITEM_CLOSE;

public class SourceTokSpecial extends DecoratorBase {
    private final String spaceOpen, spaceClose;
    private final Queue<IReadNode> queue;
    IReadNode nextNode, currNode;

    public SourceTokSpecial(IDataSource dataSource) {
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
            if(Glob.TOK_SPECIAL.tryTok(text)){
                ArrayList<String> tokens =  Glob.TOK_SPECIAL.getResult();
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
