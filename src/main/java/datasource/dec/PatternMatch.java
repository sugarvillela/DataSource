package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;
import runstate.Glob;
import textevent.iface.ITextEventNode;

import java.util.Map;

import static runstate.Glob.LANG_STRUCT_UTIL;

/** Matches text with enums defined in TEXT_PATTERN
 *  If exact match, node.textEvent is updated with the enum so later parsing can respond to it */
public class PatternMatch extends DecoratorBase{// TODO change class name
    private final Map<String, ITextEventNode> map;

    public PatternMatch(IDataSource dataSource) {
        super(dataSource);
        map = Glob.ENUMS_BY_TYPE.getTextPatternsAsMap();
    }

    @Override
    public IReadNode next() {
        IReadNode node = dataSource.next();
        if(node != null && !node.hasTextEvent()){
            ITextEventNode event;
            String text = node.text();
            if((event = map.get(text)) != null){
                node.setLangStruct(event);
            }
            else if((event = map.get(text.substring(0, 1))) != null && event.textPattern().useSubstring()){
                node.setLangStruct(event);
                node.setText(text.substring(1));
            }
        }
        return node;
    }
}
