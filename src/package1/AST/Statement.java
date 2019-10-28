package package1.AST;

import package1.AST.TOKENS.Operator;

public class Statement extends AST {
    Operation operation;
    MethodCall methodCall;

    public Statement(Operation operation) {
        this.operation = operation;
    }

    public Statement(MethodCall methodCall) {
        this.methodCall = methodCall;
    }
}
