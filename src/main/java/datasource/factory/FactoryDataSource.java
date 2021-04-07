package datasource.factory;

import datasource.dec.*;
import datasource.dec_fluid.SourceFluid;
import datasource.dec_tok.SourceTok;
import datasource.iface.IDataSource;
import datasource.core.SourceArray;
import datasource.core.SourceList;
import datasource.core.SourceFile;
import datasource.core_largefile.SourceLargeFile;

import java.util.List;

/** Demonstrates the suggested decorator order.  Passing null for an event parameter removes a layer from
 *  the wrap */
public class FactoryDataSource {
    private static FactoryDataSource instance;

    public static FactoryDataSource initInstance(){
        return (instance == null)? (instance = new FactoryDataSource()): instance;
    }

    private FactoryDataSource(){}


    private IDataSource getSmall1(IDataSource dataSource){
        return new SourceNonComment(
                new SourceTextEvent(
                        new SourceTok(
                                new SourceNonEmpty(dataSource))
                )
        );
    }

    private IDataSource getAll(IDataSource dataSource){
        return new SourcePeek(
                new SourceActiveOnly(
                        new SourceFluid(
                                new SourceNonComment(
                                        new SourceTextEvent(
                                                new SourceTok(
                                                        new SourceNonEmpty(dataSource))
                                        )
                                )
                        )
                )
        );
    }

    public IDataSource getSourceSmall(String[] array){
        return this.getAll(new SourceArray(array));
    }

    public IDataSource getSourceSmall(List list){
        return this.getAll(new SourceList(list));
    }

    public IDataSource getSourceSmall(String fileName){
        return this.getAll(new SourceFile(fileName));
    }

    public IDataSource getSourceLargeFileSmall(String fileName){
        return this.getAll(new SourceLargeFile(fileName));
    }



    public IDataSource getSource(String[] array){
        return this.getAll(new SourceArray(array));
    }

    public IDataSource getSource(List list){
        return this.getAll(new SourceList(list));
    }

    public IDataSource getSource(String fileName){
        return this.getAll(new SourceFile(fileName));
    }

    public IDataSource getSourceLargeFile(String fileName){
        return this.getAll(new SourceLargeFile(fileName));
    }
}
