package datasource.factory;

import datasource.dec.PatternMatch;
import datasource.dec_fluid.SourceFluid;
import datasource.dec.SourceNonEmpty;
import datasource.dec.SourcePeek;
import datasource.dec_tok.SourceTok;
import datasource.iface.IDataSource;
import datasource.core.SourceArray;
import datasource.core.SourceList;
import datasource.core.SourceFile;
import datasource.core_largefile.SourceLargeFile;
import visitor.impl.EventProviderFluid;

import java.util.List;

/** Demonstrates the suggested decorator order.  Passing null for an event parameter removes a layer from
 *  the wrap */
public class FactoryDataSource {
    private static FactoryDataSource instance;

    public static FactoryDataSource initInstance(){
        return (instance == null)? (instance = new FactoryDataSource()): instance;
    }

    private FactoryDataSource(){}

    private IDataSource getWrap(IDataSource dataSource, EventProviderFluid fluidEvent){
        return new SourcePeek(
                new SourceFluid(
                        new PatternMatch(
                                new SourceTok(
                                        new SourceNonEmpty(dataSource))
                        ), fluidEvent
                )
        );
    }

    private IDataSource getWrap(IDataSource dataSource){
        return new SourceTok(
                new SourceNonEmpty(
                        dataSource
                )
        );
    }

    private IDataSource nullCheck(IDataSource dataSource, EventProviderFluid fluidEvent){
        return ((fluidEvent == null))? getWrap(dataSource) : getWrap(dataSource, fluidEvent);
    }

    public IDataSource getSourceTok(String[] array, EventProviderFluid fluidEvent){
        return this.nullCheck(new SourceArray(array), fluidEvent);
    }

    public IDataSource getSourceTok(List list, EventProviderFluid fluidEvent){
        return this.nullCheck(new SourceList(list), fluidEvent);
    }

    public IDataSource getSourceTok(String fileName, EventProviderFluid fluidEvent){
        return this.nullCheck(new SourceFile(fileName), fluidEvent);
    }

    public IDataSource getSourceLargeFileTok(String fileName, EventProviderFluid fluidEvent){
        return this.nullCheck(new SourceLargeFile(fileName), fluidEvent);
    }

    public IDataSource getSource(String[] array, EventProviderFluid fluidEvent){
        return this.nullCheck(new SourceArray(array), fluidEvent);
    }

    public IDataSource getSource(List list, EventProviderFluid fluidEvent){
        return this.nullCheck(new SourceList(list), fluidEvent);
    }

    public IDataSource getSource(String fileName, EventProviderFluid fluidEvent){
        return this.nullCheck(new SourceFile(fileName), fluidEvent);
    }

    public IDataSource getSourceLargeFile(String fileName, EventProviderFluid fluidEvent){
        return this.nullCheck(new SourceLargeFile(fileName), fluidEvent);
    }
}
