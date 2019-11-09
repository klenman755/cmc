package package1.AST;

public class Statement extends AST {
    public Operation operation;
    public MethodCall methodCall;

    public Object visit(Visitor v, Object arg) {
        return v.visitStatement( this, arg );
    }
}
