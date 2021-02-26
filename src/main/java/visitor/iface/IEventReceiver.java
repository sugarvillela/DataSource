package visitor.iface;

import datasource.iface.IDataSource;

/** A simple interface for a visitor-style interaction.
 *  Add more receive overloads as needed */
public interface IEventReceiver {
    void receive(IEventProvider provider, boolean data);
    void receive(IEventProvider provider, IDataSource dataSource);
}
