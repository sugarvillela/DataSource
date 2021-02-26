package visitor.iface;

import readnode.iface.IReadNode;

/** A simple interface for a visitor-style interaction.
 *  Use no-arg provide() to get defaults, arg versions to respond to request data */
public interface IEventProvider {
    void provide(IEventReceiver receiver);
    void provide(IEventReceiver receiver, IReadNode requestData);
}
