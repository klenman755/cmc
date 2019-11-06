package package1.AST;

public class Statement extends AST {
    Operation operation;
    MethodCall methodCall;

    public Statement(Operation operation) {
        this.operation = operation;
    }

    public Statement(MethodCall methodCall) {
        this.methodCall = methodCall;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitStatement( this, arg );
    }
}
