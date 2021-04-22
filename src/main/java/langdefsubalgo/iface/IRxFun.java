package langdefsubalgo.iface;

public interface IRxFun {

    interface IRxFunBuilder{
        IRxFunBuilder mainText(String mainText);
        IRxFunBuilder funType(String funType);
        IRxFunBuilder intValues(int[] intValues);
        IRxFunBuilder stringValues(String[] stringValues);
        IRxFun build();
    }
}
