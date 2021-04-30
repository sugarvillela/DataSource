package generictree.impl;

import generictree.node.ParseTreeNode;

/** For cases where the path to an element is known.
 *  Path is a 'splitChar' separated string that corresponds to
 *  the node identifiers on the path to the element (see tests)
 * @param <T> the IGTreeNode payload type
 */
public class PathTree <T> extends GTreeBase <T> {
    private final char splitChar;

    public PathTree(char splitChar) {
        this.splitChar = splitChar;
    }

    @Override
    public boolean put(T payload, String... path) {
        //System.out.println(">>>>>>>" + path);
        path = tokenizePathOnSingle(splitChar, path);
        if(root == null){
            root = new ParseTreeNode<>();
            root.setLevel(0);
            root.setIdentifier(path[0]);
            root.setPayload(payload);
            return true;
        }
        else{
            return parseObject.putByPath(payload, 0, root, path);
        }
    }
}
