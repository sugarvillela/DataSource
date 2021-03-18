package stackpayload.iface;

import readnode.iface.IReadNode;

public interface IPayloadState {
    void init(IStackPayload parentPayload);

    void set(int state);
    void set(String string);

    int getInt();
    String getString();

    void setPushedReadNode(IReadNode pushedReadNode);   // the node that created the payload
    IReadNode getPushedReadNode();

//    void onPushAbove();     // increment the max stacked above this payload
//    int getMaxAbove();
//
//    void onPushAdjAbove();  // increment the max immediate children this payload has
//    int getMaxAdjAbove();

    void incTimeOnStack();
    int timeOnStack();
}
