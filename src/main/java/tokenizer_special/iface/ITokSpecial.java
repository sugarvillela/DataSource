package tokenizer_special.iface;

import java.util.ArrayList;
import java.util.regex.Pattern;

public interface ITokSpecial {
    void initPatterns(Pattern... patterns);
    boolean tryTok(String text);
    ArrayList<String> getResult();
}
