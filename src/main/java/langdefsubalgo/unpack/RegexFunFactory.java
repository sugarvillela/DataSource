package langdefsubalgo.unpack;

import langdefsubalgo.iface.IUnpackStrategy;
import langdefsubalgo.impl.RxFun;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexFunFactory {
    private Pattern pattern;
    private IUnpackStrategy strategy;
    private int[] groups;
    private Matcher matcher;

    private RegexFunFactory(){}

    public boolean find(String text){
        return (matcher = pattern.matcher(text)).find();
    }
    public RxFun unpack(){
        String[] groupText = new String[groups.length];
        for(int i = 0; i < groups.length; i++){
            groupText[i] = matcher.group(groups[i]);
        }
        //this.strategy.unpack(fun, this);
        return null;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private final RegexFunFactory built;
        public Builder(){
            built = new RegexFunFactory();
        }
        public Builder p(Pattern pattern){
            built.pattern = pattern;
            return this;
        }
        public Builder g(int... groups){
            built.groups = groups;
            return this;
        }
        public Builder s(IUnpackStrategy strategy){
            built.strategy = strategy;
            return this;
        }
        public RegexFunFactory build(){
            if(built.groups == null){
                built.groups = new int[]{};
            }
            return built;
        }
    }
}
