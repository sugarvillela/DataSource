package datasink.iface;

import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;

import java.util.ArrayList;

/** Find and access multiple DataSinkNode objects */
public interface IDataSink extends IDataSearch {

    void put();
    void put(IReadNode readNode);
    IDataSinkNode addNewSink(String identifier, LANG_STRUCT parentEnum);

    ArrayList<IDataSinkNode> allSinks();
    ArrayList<IDataSinkNode> groupSinksByEnum(LANG_STRUCT... parentEnums);
    ArrayList<IDataSinkNode> excludeSinksByEnum(LANG_STRUCT... parentEnums);
    void pruneSinksToEnum(LANG_STRUCT... parentEnums);
}
