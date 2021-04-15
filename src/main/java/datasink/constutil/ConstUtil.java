package datasink.constutil;

import datasink.iface.IDataSinkNode;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import runstate.Glob;

import java.util.ArrayList;

public class ConstUtil {
    private static ConstUtil instance;

    public static ConstUtil initInstance(){
        return (instance == null)? (instance = new ConstUtil()): instance;
    }

    private final LANG_STRUCT enumConstant;

    private ConstUtil() {
        enumConstant = Glob.ENUMS_BY_TYPE.enumConstant();
    }

    public IReadNode getIdentifier(String identifier){
        IDataSinkNode sinkNode = Glob.DATA_SINK.getIdentifier(identifier);
        if(sinkNode != null && sinkNode.getParentEnum() == enumConstant){
            ArrayList<IReadNode> readNodes = sinkNode.getNodes();
            return (readNodes.size() == 1)? readNodes.get(0) : null;
        }
        return null;
    }
}
