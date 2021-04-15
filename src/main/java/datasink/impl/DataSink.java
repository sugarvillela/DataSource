package datasink.impl;

import datasink.iface.IDataSearch;
import datasink.iface.IDataSinkNode;
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

    private DataSink() {
        sinks = new ArrayList<>();

        // initialize with one sinkNode for the root language sink
        LANG_STRUCT langRoot1 = Glob.ENUMS_BY_TYPE.enumLangRoot1();
        //LANG_STRUCT langRoot2 = Glob.ENUMS_BY_TYPE.langRootEnum2();
        dataSearch = new DataSearch(
            langRoot1.toString(), this.addNewSink(langRoot1.toString(), langRoot1)//,
            //langRoot2.toString(), this.addNewSink(langRoot2.toString(), langRoot2)
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
        IDataSinkNode newSink = new DataSinkNode(identifier, parentEnum);
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
    public IDataSinkNode getIdentifierOrErr() {
        return dataSearch.getIdentifierOrErr();
    }

    @Override
    public IDataSinkNode getIdentifierOrErr(IReadNode readNode) {
        return dataSearch.getIdentifierOrErr(readNode);
    }

    @Override
    public IDataSinkNode getIdentifierOrErr(String identifier) {
        return dataSearch.getIdentifierOrErr(identifier);
    }

    @Override
    public IDataSinkNode getIdentifier(String identifier) {
        return dataSearch.getIdentifier(identifier);
    }
}
