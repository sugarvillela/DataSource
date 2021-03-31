package runstate.iface;

public interface IRunState extends IRunStep {
    void setInFilePath(String filePath);
    IRunStep currentSourceStep();

    /* Program state set */
    void initRunState();
    void initStep1();
    void runPreScan();

}
