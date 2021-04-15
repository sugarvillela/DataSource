package runstate.iface;

public interface IRunState extends IRunStep {
    void setInFilePath(String filePath);
    IRunStep currentSourceStep();

    /* Program state set */
    void initRunState();
    void initTest();// for testing only
    void initStep1();
    void initStep2();

}
