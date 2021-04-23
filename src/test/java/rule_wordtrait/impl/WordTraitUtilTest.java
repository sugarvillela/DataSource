package rule_wordtrait.impl;

import err.ERR_TYPE;
import langdefsub.PAR_TYPE;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rule_wordtrait.WordTraitRule;
import rule_wordtrait.iface.ITraitPatternUtil;
import rule_wordtrait.iface.IWordTraitClient;
import rule_wordtrait.iface.ICharTraitParser;

import java.util.Arrays;

import static rule_wordtrait.impl.CharTraitImplGroup.*;
import static langdefsub.PAR_TYPE.*;
import static rule_wordtrait.impl.WordTraitActionImplGroup.*;

class WordTraitUtilTest {

    private ICharTraitParser getWordTraitUtil(){
        return CharTraitParser.builder().skipSymbols('\'').
                traits(
                        new CharTrait('\''),
                        new CharTrait('.'),
                        new CharTrait(','),
                        new CharTrait(':'),
                        new CharTrait('*'),
                        new CharTrait('['),
                        new CharTrait('_'),
                        new CharTraitAlpha('A'),
                        new CharTraitVisibleAscii('C'),
                        new CharTraitNumeric('N')
                ).build();
    }

    @Test
    void givenWordTraitUtil_returnAllTraitsAsString(){
        ICharTraitParser wordTrait = getWordTraitUtil();
        String actual, expected;
        actual = wordTrait.getWatchedTraits();
        expected = "'*,.:ACN[_";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenStringWithTraits_returnTraitString(){
        ICharTraitParser wordTrait = getWordTraitUtil();
        String text, actual, expected;

        text = "23";// num par
        expected = "CN";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "2,3,4"; // num list
        expected = ",CN";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "'a'"; // string
        expected = "'C";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "'Larry','Moe','Curly'"; // string list
        expected = "',C";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "abc_1"; // identifier
        expected = "ACN_";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "A,B_2,myVar"; // identifier list
        expected = ",ACN_";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "path1.path2.path_3";// identifier path
        expected = ".ACN_";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "2:3"; // numeric range
        expected = ":CN";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "*abc_1"; // id access
        expected = "*ACN_";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "*[2]"; // fx access
        expected = "*CN[";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);

        text = "*[8:11]"; // id access
        expected = "*:CN[";
        actual = wordTrait.setText(text).parse().getFoundTraits();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenOddballInput_returnCorrect(){
        ICharTraitParser wordTrait = getWordTraitUtil();
        String text, actual, expected;
        text = "''";// string par
        actual = wordTrait.setText(text).parse().getFoundTraits();
        expected = "'C";
        Assertions.assertEquals(expected, actual);

        text = "'',''";// string par
        actual = wordTrait.setText(text).parse().getFoundTraits();
        expected = "',C";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenFormattedString_sortInString(){
        ITraitPatternUtil traitPatternUtil = TraitPatternUtil.initInstance();
        String text, expected, actual;
        text = "vtdajcrwb";
        expected = "abcdjrtvw";
        actual = traitPatternUtil.sortText(text);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenPattern_sortByOddChars(){
        ITraitPatternUtil traitPatternUtil = TraitPatternUtil.initInstance();
        String text, expected, actual;
        text = ".a-c+b+e.d";
        expected = ".a+b-c.d+e";
        actual = traitPatternUtil.sortPattern(text);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenTextAndPattern_matchPattern(){
        ITraitPatternUtil traitPatternUtil = TraitPatternUtil.initInstance();
        String text, pattern;
        boolean actual;
        text = "abcd";
        pattern = "+a+b+c+d";
        pattern = traitPatternUtil.sortPattern(pattern);
        actual = traitPatternUtil.match(text, pattern);
        Assertions.assertTrue(actual);

        text = "abd";
        pattern = "+a+b.c+d";
        pattern = traitPatternUtil.sortPattern(pattern);
        actual = traitPatternUtil.match(text, pattern);
        Assertions.assertTrue(actual);

        text = "abd";
        pattern = "+a+b-c+d";
        pattern = traitPatternUtil.sortPattern(pattern);
        actual = traitPatternUtil.match(text, pattern);
        Assertions.assertTrue(actual);

        text = "efg";
        pattern = ".a.b-c.d.e.f.g";
        pattern = traitPatternUtil.sortPattern(pattern);
        actual = traitPatternUtil.match(text, pattern);
        Assertions.assertTrue(actual);
    }

    @Test
    void givenTextAndPattern_noMatchPattern(){
        ITraitPatternUtil traitPatternUtil = TraitPatternUtil.initInstance();
        String text, pattern;
        boolean actual;
        text = "abcd";
        pattern = "+a+b-c+d";
        pattern = traitPatternUtil.sortPattern(pattern);
        actual = traitPatternUtil.match(text, pattern);
        Assertions.assertFalse(actual);

        text = "abd";
        pattern = "+a+b+c+d";
        pattern = traitPatternUtil.sortPattern(pattern);
        actual = traitPatternUtil.match(text, pattern);
        Assertions.assertFalse(actual);

        text = "abfd";
        pattern = "+a+b+c+d";
        pattern = traitPatternUtil.sortPattern(pattern);
        actual = traitPatternUtil.match(text, pattern);
        Assertions.assertFalse(actual);
    }

    private WordTraitRule initRule(){
        ICharTraitParser parser = CharTraitParser.builder().
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
        WordTraitRule rule = new WordTraitRule();
        rule.initWordTraitRule(
            new TraitPatternMatch(
                parser, "-'-*-,-.-:-A+C+N-[-_", NUM_PAR, WordTraitActionNumber.initInstance()
            ),
            new TraitPatternMatch(
                parser, "-'-*+,-.-:-A+C+N-[-_", NUM_LIST, WordTraitActionNumbers.initInstance(',')
            ),
            new TraitPatternMatch(
                parser, "+'-*-,-.-:-A+C-N-[-_", STR_PAR, WordTraitActionQuoted.initInstance()
            ),
            new TraitPatternMatch(
                parser, "+'-*+,-.-:-A+C-N-[-_", STR_LIST, WordTraitActionQuotedList.initInstance()
            ),
            new TraitPatternMatch(
                parser, "-'-*-,-.-:+A+C.N-[._", ID_PAR, WordTraitActionString.initInstance()
            ),
            new TraitPatternMatch(
                parser, "-'-*+,-.-:+A+C.N-[._", ID_LIST, WordTraitActionStrings.initInstance(',')
            ),
            new TraitPatternMatch(
                parser, "-'-*-,+.-:+A+C.N-[._", ID_SEP, WordTraitActionStrings.initInstance('.')
            ),
            new TraitPatternMatch(
                parser, "-'+*-,-.-:+A+C.N-[._", STAR_ID, WordTraitActionString.initInstance()
            ),
            new TraitPatternMatch(
                parser, "-'-*-,-.+:-A+C+N-[-_", NUM_RANGE, WordTraitActionNumbers.initInstance(':')
            ),
            new TraitPatternMatch(
                parser, "-'+*-,-.-:-A+C+N+[-_", STAR_NUM, WordTraitActionNumber.initInstance()
            ),
            new TraitPatternMatch(
                parser, "-'+*-,-.+:-A+C+N+[-_", STAR_RANGE, WordTraitActionNumbers.initInstance(':')
            )
        );
        return rule;
    }

    @Test
    void givenText_extractContent(){
        WordTraitRule rule = initRule();
        IWordTraitClient client = new TestClient();
        String text, expected, actual;
        ERR_TYPE expectedErrType = ERR_TYPE.NONE;
        ERR_TYPE actualErrType;

        text = "23";
        actualErrType = rule.tryParse(client, text);
        expected = "strings=null|numbers=[23]|parTypeEnum=NUM_PAR";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "2,3,4"; // num list
        actualErrType = rule.tryParse(client, text);
        expected = "strings=null|numbers=[2, 3, 4]|parTypeEnum=NUM_LIST";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "'a'"; // string
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[a]|numbers=null|parTypeEnum=STR_PAR";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "'Larry','Moe','Curly'"; // string list
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[Larry, Moe, Curly]|numbers=null|parTypeEnum=STR_LIST";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "abc_1"; // identifier
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[abc_1]|numbers=null|parTypeEnum=ID_PAR";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "MyId"; // identifier
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[MyId]|numbers=null|parTypeEnum=ID_PAR";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "id1"; // identifier
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[id1]|numbers=null|parTypeEnum=ID_PAR";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "A,B_2,myVar"; // identifier list
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[A, B_2, myVar]|numbers=null|parTypeEnum=ID_LIST";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "A,B,myVar"; // identifier list
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[A, B, myVar]|numbers=null|parTypeEnum=ID_LIST";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "path1.path2.path_3";// identifier path
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[path1, path2, path_3]|numbers=null|parTypeEnum=ID_SEP";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "2:3"; // numeric range
        actualErrType = rule.tryParse(client, text);
        expected = "strings=null|numbers=[2, 3]|parTypeEnum=NUM_RANGE";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        text = "*abc_1"; // id access
        actualErrType = rule.tryParse(client, text);
        expected = "strings=[*abc_1]|numbers=null|parTypeEnum=STAR_ID";
        actual = client.toString();
        Assertions.assertEquals(expectedErrType, actualErrType);
        Assertions.assertEquals(expected, actual);

        // bad test
        text = "'Larry','Mo'e','Curly'"; // string list
        actualErrType = rule.tryParse(client, text);
        Assertions.assertEquals(ERR_TYPE.UNKNOWN_PATTERN, actualErrType);
    }

    public static class TestClient implements IWordTraitClient {
        private String[] strings;
        private int[] numbers;
        private PAR_TYPE parTypeEnum;

        @Override
        public void receiveContent(String... content) {
            strings = content;
            numbers = null;
            //System.out.println("receive content: string");
            //System.out.println(Arrays.toString(content));
        }

        @Override
        public void receiveContent(int... content) {
            numbers = content;
            strings = null;
            //System.out.println("receive content: int");
            //System.out.println(Arrays.toString(content));
        }

        @Override
        public void receiveContent(PAR_TYPE content) {
            parTypeEnum = content;
            //System.out.println("receive content: enum");
            //System.out.println(content);
        }

        @Override
        public String toString() {
            return
                    "strings=" + Arrays.toString(strings) +
                    "|numbers=" + Arrays.toString(numbers) +
                    "|parTypeEnum=" + parTypeEnum;
        }
    }
}