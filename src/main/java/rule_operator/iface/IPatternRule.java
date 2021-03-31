package rule_operator.iface;

import readnode.iface.IReadNode;

import java.util.ArrayList;
import java.util.Queue;

public interface IPatternRule {
    IPatternRule initPatterns(IActionPattern... patterns);
    IPatternRule initAntiPatterns(IActionPattern... antiPatterns);

    boolean shouldDoAction(String text);
    ArrayList<String> doListAction(String text);

    String operatorText();
}
