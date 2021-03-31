package generictree.iface;

public interface IGTree <T>{
    IGTreeNode <T> getRoot();
    boolean put(String path);
    boolean put(String path, T payload);


    IGTreeParse<T> getParse();
}
