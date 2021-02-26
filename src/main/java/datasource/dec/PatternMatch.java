package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;
import textpattern.TEXT_PATTERN;

/** Matches text with enums defined in TEXT_PATTERN
 *  If exact match, node.textPattern is updated with the enum so later parsing can respond to it */
public class PatternMatch extends DecoratorBase{
    public PatternMatch(IDataSource dataSource) {
        super(dataSource);
    }
    @Override
    public IReadNode next() {
        IReadNode node = dataSource.next();

        if(node != null){
            node.setTextPattern(TEXT_PATTERN.getEnum(node.text()));
        }
        return node;
    }
}
