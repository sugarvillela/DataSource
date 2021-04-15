package datasink.iface;

import datasink.iface.IDataSinkNode;
import readnode.iface.IReadNode;

public interface IDataSearch {
    boolean haveIdentifier();
    boolean haveIdentifier(IReadNode readNode);
    boolean haveIdentifier(String identifier);

    void setIdentifier();
    void setIdentifier(IReadNode readNode);

    IDataSinkNode getIdentifierOrErr();
    IDataSinkNode getIdentifierOrErr(IReadNode readNode);
    IDataSinkNode getIdentifierOrErr(String identifier);

    IDataSinkNode getIdentifier(String identifier);
}
