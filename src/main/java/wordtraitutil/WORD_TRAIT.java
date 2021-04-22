package wordtraitutil;

import err.ERR_TYPE;
import wordtraitutil.iface.IWordTraitAction;
import wordtraitutil.iface.ITraitPatternUtil;
import wordtraitutil.iface.IWordTraitParser;
import wordtraitutil.iface.IWordTraitClient;
import wordtraitutil.impl.CharTraitImplGroup;
import wordtraitutil.impl.TraitPatternUtil;
import wordtraitutil.impl.WordTraitParser;

import static wordtraitutil.impl.WordTraitActionImplGroup.*;

public enum WORD_TRAIT {
    NUM_PAR         ("-'-*-,-.-:-A+C+N-[-_", WordTraitActionNumber.initInstance()),   // "CN"
    NUM_LIST        ("-'-*+,-.-:-A+C+N-[-_", WordTraitActionNumbers.initInstance(',')),   // ",CN"
    STR_PAR         ("+'-*-,-.-:-A+C-N-[-_", WordTraitActionQuoted.initInstance()),   // "'C"
    STR_LIST        ("+'-*+,-.-:-A+C-N-[-_", WordTraitActionQuotedList.initInstance()),   // "',C"
    ID_PAR          ("-'-*-,-.-:+A+C.N-[._", WordTraitActionString.initInstance()),   // "ACN_"
    ID_LIST         ("-'-*+,-.-:+A+C.N-[._", WordTraitActionStrings.initInstance(',')),   // ",ACN_"
    ID_SEP          ("-'-*-,+.-:+A+C.N-[._", WordTraitActionStrings.initInstance('.')),   // ".ACN_"
    STAR_ID         ("-'+*-,-.-:+A+C.N-[._", WordTraitActionString.initInstance()),   // *ACN_
    NUM_RANGE       ("-'-*-,-.+:-A+C+N-[-_", WordTraitActionNumbers.initInstance(':')),   // ":CN"
    STAR_NUM        ("-'+*-,-.-:-A+C+N+[-_", WordTraitActionNumber.initInstance()),   // "*CN["
    STAR_RANGE      ("-'+*-,-.+:-A+C+N+[-_", WordTraitActionNumbers.initInstance(':')),   // "*:CN["
    ;

    private static final IWordTraitParser wordTraitParser = WordTraitParser.builder().
        skipSymbols('\'').keepEscapeSymbol().
        traits(
                new CharTraitImplGroup.CharTrait('\''),
                new CharTraitImplGroup.CharTrait('.'),
                new CharTraitImplGroup.CharTrait(','),
                new CharTraitImplGroup.CharTrait(':'),
                new CharTraitImplGroup.CharTrait('*'),
                new CharTraitImplGroup.CharTrait('['),
                new CharTraitImplGroup.CharTrait('_'),
                new CharTraitImplGroup.CharTraitAlpha('A'),
                new CharTraitImplGroup.CharTraitVisibleAscii('C'),
                new CharTraitImplGroup.CharTraitNumeric('N')
        ).build();

    private final ITraitPatternUtil traitPatternUtil;
    private final IWordTraitAction extract;
    private final String p;

    WORD_TRAIT(String p, IWordTraitAction extract) {
        this.extract = extract;
        traitPatternUtil = TraitPatternUtil.initInstance();
        this.p = traitPatternUtil.sortPattern(p);
    }

    public static IWordTraitParser getWordTraitParser(){
        return wordTraitParser;
    }

    public static ERR_TYPE tryParse(IWordTraitClient client, String text) {
        String traitText = wordTraitParser.setText(text).parse().getFoundTraits();
        WORD_TRAIT traitEnum = fromTraitText(traitText);
        if(traitEnum != null){
            client.receiveContent(traitEnum);
            return traitEnum.extract.doAction(client, text);
        }
        return ERR_TYPE.UNKNOWN_PATTERN;
    }

    public static WORD_TRAIT fromTraitText(String text){
        for(WORD_TRAIT wordTraitEnum : values()){
            if(wordTraitEnum.traitPatternUtil.match(text, wordTraitEnum.p)){
                return wordTraitEnum;
            }
        }
        return null;
    }
}
