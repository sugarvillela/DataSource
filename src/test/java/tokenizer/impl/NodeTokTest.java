package tokenizer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import readnode.iface.IReadNode;
import tokenizer.iface.ITokenizer;
import tokenizer.toknode.NodeTokenizer;

import java.util.ArrayList;

class NodeTokTest {

    @Test
    void simpleTokShouldIgnoreExtraSpaces(){
        ITokenizer tokenizer = new SimpleTok();
        String text = "String  with too many      spaces    ";
        String[] tokens = tokenizer.setText(text).parse().toArray();
        String actual = String.join("|", tokens);
        String expected =
            "String|" +
            "with|" +
            "too|" +
            "many|" +
            "spaces";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void nodeTokShouldIgnoreExtraSpaces(){
        NodeTokenizer tokenizer = new NodeTokenizer();
        String text = "   \tString  with too many      spaces    ";

        // container values here are random; in operation the values come from the containing source
        IReadNode[] nodes = tokenizer.parse(text, "test.txt", 2, true).getArray();

        ArrayList<String> data = new ArrayList<>();
        for(IReadNode node : nodes){
            data.add(node.csvString());
        }
        String actual = String.join("|", data);
        String expected =
            "test.txt,2,0,7,1,0,1,String,-|" +
            "test.txt,2,1,2,1,0,1,with,-|" +
            "test.txt,2,2,1,1,0,1,too,-|" +
            "test.txt,2,3,1,1,0,1,many,-|" +
            "test.txt,2,4,6,1,1,1,spaces,-";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void nodeTokShouldSkipAreas(){
        NodeTokenizer tokenizer = new NodeTokenizer();
        String text = "   \tString  with too   'many      spaces'    ";

        // container values here are random; in operation the values come from the containing source
        IReadNode[] nodes = tokenizer.parse(text, "test.txt", 2, true).getArray();

        ArrayList<String> data = new ArrayList<>();
        for(IReadNode node : nodes){
            data.add(node.csvString());
        }
        String actual = String.join("|", data);
        String expected =
            "test.txt,2,0,7,1,0,1,String,-|"+
            "test.txt,2,1,2,1,0,1,with,-|"+
            "test.txt,2,2,1,1,0,1,too,-|"+
            "test.txt,2,3,3,1,1,1,many      spaces,-";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void nodeTokShouldRecreateString(){
        NodeTokenizer tokenizer = new NodeTokenizer();

        // won't pass with spaces at end because those are to be discarded
        String text = "    String  with too many      spaces";
        IReadNode[] nodes = tokenizer.parse(text, "test.txt", 2, true).getArray();

        ArrayList<String> data = new ArrayList<>();
        for(IReadNode token : nodes){
            data.add(token.indentedText());
        }

        String actual = String.join("", data);
        Assertions.assertEquals(text, actual);
    }
    @Test
    void nodeTokFailRecreateStringOnTab(){//tabs are converted to 4 spaces
        NodeTokenizer tokenizer = new NodeTokenizer();

        // won't pass with spaces at end because those are to be discarded
        String text = "    String  with\ttabs and \tspaces";
        IReadNode[] nodes = tokenizer.parse(text, "test.txt", 2, true).getArray();

        ArrayList<String> data = new ArrayList<>();
        for(IReadNode token : nodes){
            data.add(token.indentedText());
        }
        String expected = "    String  with    tabs and     spaces";
        String actual = String.join("", data);
        Assertions.assertEquals(expected, actual);
    }
}