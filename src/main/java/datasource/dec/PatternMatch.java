package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

import static runstate.Glob.LANG_STRUCT_UTIL;

/** Matches text with enums defined in TEXT_PATTERN
 *  If exact match, node.langStruct is updated with the enum so later parsing can respond to it */
public class PatternMatch extends DecoratorBase{
    public PatternMatch(IDataSource dataSource) {
        super(dataSource);
    }
    @Override
    public IReadNode next() {
        IReadNode node = dataSource.next();

        if(node != null && LANG_STRUCT_UTIL.isLangStruct(node.text())){
            node.setLangStruct(LANG_STRUCT_UTIL.getLastMatch());
        }

        return node;
    }
}
