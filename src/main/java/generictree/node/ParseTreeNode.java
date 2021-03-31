package generictree.node;

import generictree.iface.IGTreeNode;

public class ParseTreeNode<T> extends GTreeNodeBase<T> {
    //level, isLeaf(B or L), children.size, op, negated, identifier, parent.identifier, payload.toString
    private static final String FRIENDLY_FORMAT = "%s, %s, %s, %s, %s, %s, %s, %s";
    //level, isLeaf(B or L), children.size, op, negated, identifier, payload.toString
    private static final String CSV_FORMAT = "%d,%d,%d,%s,%s,%s,%s";

    private char op;
    private boolean negated, wrapped;

    public ParseTreeNode(){}

    @Override
    public void setOp(char op) {
        this.op = op;
    }

    @Override
    public char op() {
        return op;
    }

    @Override
    public void setNegated(boolean negated) {
        this.negated = negated;
    }

    @Override
    public boolean negated() {
        return negated;
    }

    @Override
    public void setWrapped(boolean wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean wrapped() {
        return wrapped;
    }

    @Override
    public void addChild(IGTreeNode<T> child) {
        child.setParent(this);
        child.setLevel(level + 1);
        children.add(child);
    }

    @Override
    public void addChild(String identifier, T payload) {
        IGTreeNode<T> child = new ParseTreeNode<>();
        child.setIdentifier(identifier);
        child.setPayload(payload);
        this.addChild(child);
    }


    @Override
    public String friendlyString() {
        //level, leaf, children.size, op, negated, identifier, parent.identifier, payload.toString
        return String.format(FRIENDLY_FORMAT,
                String.format("(level: %d)", level),
                (this.isLeaf()? "leaf" : "bran"),
                String.format("(children: %d)", children.size()),
                String.format("(op: %s)", ((op == '\0')? NULL_STRING : String.valueOf(op))),
                String.format("(negated: %s)", (negated? "!" : NULL_STRING)),
                String.format("(id: %s)", this.friendlySelfInfoString()),
                String.format("(parent: %s)", this.friendlyParentInfoString()),
                ((payload == null)? NULL_STRING : payload.toString())
        );
    }

    @Override
    public String csvString() {
        //level, isLeaf(B or L), children.size, op, negated, identifier, parent.identifier, payload.toString
        return String.format(CSV_FORMAT,
            level,
            (this.isLeaf()? 1 : 0),
            children.size(),
            ((op == '\0')? NULL_STRING : String.valueOf(op)),
            (negated? "!" : NULL_STRING),
            (identifier.isEmpty())? NULL_STRING : identifier,
            ((payload == null)? NULL_STRING : payload.toString())
        );
    }

    private String friendlyParentInfoString(){
        if(parent != null){
            if(parent.parent() == null){
                return "root";
            }
            if(parent.identifier() != null && !parent.identifier().isEmpty()){
                return parent.identifier();
            }
            if(parent.op() != '\0'){
                return String.valueOf(parent.op());
            }
        }
        return NULL_STRING;
    }
    private String friendlySelfInfoString(){
        if(parent == null){
            return "root";
        }
        if(identifier != null && !identifier.isEmpty()){
            return identifier;
        }
        if(op != '\0'){
            return String.valueOf(op);
        }
        return NULL_STRING;
    }
}
