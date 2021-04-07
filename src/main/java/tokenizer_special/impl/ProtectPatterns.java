package tokenizer_special.impl;

import tokenizer_special.iface.IProtectPatterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtectPatterns implements IProtectPatterns {
    private Pattern[] patterns;
    private String protectedText;

    @Override
    public void initPatterns(Pattern... patterns) {
        this.patterns = patterns;
        //System.out.println("funPat: "+patterns[0].toString());
        //System.out.println("rangePat: "+patterns[1].toString());
    }

    @Override
    public boolean tryProtect(String text) {
        Matcher matcher;
        for(Pattern pattern : patterns){
            if((matcher = pattern.matcher(text)).find()){
                //System.out.print("found: "+text+":\n");
                String match = matcher.group(0);
                protectedText = matcher.replaceAll(" " + match + " ");
                return true;
            }
        }
        return false;
    }

    @Override
    public String getResult() {
        return protectedText;
    }
}
