package rule_operator.iface;

import java.util.ArrayList;
import java.util.regex.Pattern;

public interface IActionPattern {
    IActionPattern setGroup(int... groups);
    boolean shouldDoAction(String text);
    ArrayList<String> doListAction();
}
