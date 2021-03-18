package datasink.impl;

import datasink.iface.IDataSearch;
import datasink.iface.IDataSinkNode;
import err.ERR_TYPE;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import runstate.Glob;

import java.util.HashMap;
import java.util.Map;

public class DataSearch implements IDataSearch {
    private final Map<String, IDataSinkNode> map;

    /** Initialize with one node */
    public DataSearch(String rootIdentifier, IDataSinkNode rootDataSinkNode) {
        map = new HashMap<>();
        map.put(rootIdentifier, rootDataSinkNode);
    }

    @Override
    public boolean haveIdentifier() {
        return this.haveIdentifier(Glob.RUN_STATE.getCurrNode());
    }

    @Override
    public boolean haveIdentifier(IReadNode readNode) {
        return this.haveIdentifier(readNode.textEvent().substring());
    }

    @Override
    public boolean haveIdentifier(String identifier) {
        return map.get(identifier) != null;   // name in table
    }


    @Override
    public void setIdentifier() {
        this.setIdentifier(Glob.RUN_STATE.getCurrNode());
    }

    @Override
    public void setIdentifier(IReadNode readNode) {
        if(this.haveIdentifier(readNode)){                      // check if already exists (user error)
            Glob.ERR.kill(ERR_TYPE.DUPLICATE_ID);
        }

        LANG_STRUCT parentEnum = readNode.textEvent().langStruct();                                     // get enum
        IDataSinkNode sinkNode = Glob.DATA_SINK.addNewSink(readNode.textEvent().substring(), parentEnum);// set new sink
        map.put(readNode.textEvent().substring(), sinkNode);                               // save enum, new sink in map
    }


    @Override
    public IDataSinkNode getIdentifier() {
        return this.getIdentifier(Glob.RUN_STATE.getCurrNode());
    }

    @Override
    public IDataSinkNode getIdentifier(IReadNode readNode) {
        if(!readNode.textEvent().hasSubstring()){       // delete
            Glob.ERR.kill(ERR_TYPE.DEV_ERROR);
        }
        return this.getIdentifier(readNode.textEvent().substring());
    }

    @Override
    public IDataSinkNode getIdentifier(String identifier) {
        IDataSinkNode sinkNode = map.get(identifier);
        if(sinkNode == null){                                     // check if bad identifier (user error)
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_ID);
        }
        return sinkNode;
    }
}
