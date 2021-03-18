package datasink;

import datasink.iface.IDataSearch;
import datasink.impl.DataSearch;
import datasink.iface.IDataSinkNode;
import datasink.impl.DataSinkNodeCore;
import datasink.iface.IDataSink;
import langdefalgo.iface.LANG_STRUCT;
import readnode.iface.IReadNode;
import runstate.Glob;

import java.util.ArrayList;

public class DataSink implements IDataSink {
    private static DataSink instance;

    public static DataSink initInstance(){
        return (instance == null)? (instance = new DataSink()): instance;
    }

    private final IDataSearch dataSearch;
    private ArrayList<IDataSinkNode> sinks;

    public DataSink() {
        sinks = new ArrayList<>();

        // initialize with one sinkNode for the root language sink
        LANG_STRUCT langRoot = Glob.ENUMS_BY_TYPE.langRootEnum();
        dataSearch = new DataSearch(
            langRoot.toString(), this.addNewSink(langRoot.toString(), langRoot)
        );
    }

    @Override
    public void put() {
        this.put(Glob.RUN_STATE.getCurrNode());
    }

    @Override
    public void put(IReadNode readNode) {
        readNode.renumberSortValue();
        for(IDataSinkNode dataSink : sinks){
            dataSink.put(readNode);
        }
    }

    @Override
    public IDataSinkNode addNewSink(String identifier, LANG_STRUCT parentEnum) {
        IDataSinkNode newSink = new DataSinkNodeCore(identifier, parentEnum);
        sinks.add(newSink);
        return newSink;
    }

    @Override
    public ArrayList<IDataSinkNode> allSinks() {
        return new ArrayList<>(sinks);
    }

    @Override
    public ArrayList<IDataSinkNode> groupSinksByEnum(LANG_STRUCT... parentEnums) {
        ArrayList<IDataSinkNode> out = new ArrayList<>();
        for(IDataSinkNode sink : sinks){
            for(LANG_STRUCT parentEnum : parentEnums){
                if(parentEnum == sink.getParentEnum()){
                    out.add(sink);
                }
            }
        }
        return out;
    }

    @Override
    public ArrayList<IDataSinkNode> excludeSinksByEnum(LANG_STRUCT... parentEnums) {
        if(parentEnums.length == 0){
            return allSinks();
        }
        ArrayList<IDataSinkNode> out = new ArrayList<>();
        for(IDataSinkNode sink : sinks){
            for(LANG_STRUCT parentEnum : parentEnums){
                if(parentEnum != sink.getParentEnum()){
                    out.add(sink);
                }
            }
        }
        return out;
    }

    @Override
    public void pruneSinksToEnum(LANG_STRUCT... parentEnums) {
        this.sinks = groupSinksByEnum(parentEnums);
    }


    /*=====IDataSearch delegate methods===============================================================================*/

    @Override
    public boolean haveIdentifier() {
        return dataSearch.haveIdentifier();
    }

    @Override
    public boolean haveIdentifier(IReadNode readNode) {
        return dataSearch.haveIdentifier(readNode);
    }

    @Override
    public boolean haveIdentifier(String identifier) {
        return dataSearch.haveIdentifier(identifier);
    }

    @Override
    public void setIdentifier() {
        dataSearch.setIdentifier();
    }

    @Override
    public void setIdentifier(IReadNode readNode) {
        dataSearch.setIdentifier(readNode);
    }

    @Override
    public IDataSinkNode getIdentifier() {
        return dataSearch.getIdentifier();
    }

    @Override
    public IDataSinkNode getIdentifier(IReadNode readNode) {
        return dataSearch.getIdentifier(readNode);
    }

    @Override
    public IDataSinkNode getIdentifier(String identifier) {
        return dataSearch.getIdentifier(identifier);
    }
}
