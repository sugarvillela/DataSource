package generictree.iface;

public interface IGTree <T>{
    IGTreeNode <T> getRoot();
    boolean put(String... path);
    boolean put(T payload, String... path);
    void clear();

    IGTreeParse<T> getParse();
}
