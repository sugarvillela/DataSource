package langdefsubalgo.util;

import rule_wordtrait.iface.IWordTraitClient;
import rule_wordtrait.impl.WordTraitActionImplGroup;

import java.util.List;

public class NumUtil {
    private static NumUtil instance;

    public static NumUtil initInstance() {
        return (instance == null) ? (instance = new NumUtil()) : instance;
    }

    private NumUtil() {
    }

    public boolean numberToClient(IWordTraitClient client, String text) {
        try {
            client.receiveContent(Integer.parseInt(text));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean numbersToClient(IWordTraitClient client, List<String> stringList) {
        int[] intList = new int[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            try {
                intList[i] = Integer.parseInt(stringList.get(i)); // usually, but not always, validated by WORD_TRAIT pattern match
            } catch (NumberFormatException e) {
                return false;
            }
        }
        client.receiveContent(intList);
        return true;
    }
}
