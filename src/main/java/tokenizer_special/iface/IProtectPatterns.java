package tokenizer_special.iface;

import java.util.regex.Pattern;

public interface IProtectPatterns {
    void initPatterns(Pattern... patterns);
    boolean tryProtect(String text);
    String getResult();
}
