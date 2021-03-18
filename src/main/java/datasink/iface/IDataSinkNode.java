package datasink.iface;

import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;

import java.util.ArrayList;

public interface IDataSinkNode {
    String getIdentifier(); // the same identifier used in DataSearch map, mainly for debug
    LANG_STRUCT getParentEnum();
    void setListening(boolean listening);

    void put(IReadNode readNode);
    void goBack();

    ArrayList<IReadNode> getNodes();
    void clearNodes();
}
