package langdefsubalgo.util;

import langdefsub.PAR_TYPE;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rule_wordtrait.iface.IWordTraitClient;

import java.util.Arrays;

class RangeUtilTest {

    @Test
    void translateSymbolToRange() {
        RangeUtil rangeUtil = RangeUtil.initInstance();
        TestClient client = new TestClient();
        String text, actualClient, expectedClient;
        boolean actualResult;
        text = "pattern*";
        expectedClient = "strings=null|numbers=[0, 1024]|parTypeEnum=null";
        actualResult = rangeUtil.translateSymbolToRange(client, text);
        actualClient = client.toString();
        Assertions.assertTrue(actualResult);
        Assertions.assertEquals(expectedClient, actualClient);
    }

    private static class TestClient implements IWordTraitClient {
        private String[] strings;
        private int[] numbers;
        private PAR_TYPE parTypeEnum;

        public void resetTest(){
            parTypeEnum = null;
            strings = null;
            numbers = null;
        }
        @Override
        public void receiveContent(String... content) {
            strings = content;
        }

        @Override
        public void receiveContent(int... content) {
            numbers = content;
        }

        @Override
        public void receiveContent(PAR_TYPE content) {
            parTypeEnum = content;
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