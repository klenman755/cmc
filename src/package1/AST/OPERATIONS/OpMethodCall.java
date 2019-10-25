package package1.AST.OPERATIONS;

import package1.AST.MethodCall;
import package1.AST.Operation;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;

public class OpMethodCall extends Operation {
    Identifier name;
    Operator equals;
    MethodCall method;

    public OpMethodCall(Identifier name, Operator equals, MethodCall method) {
        this.name = name;
        this.equals = equals;
        this.method = method;
    }
}
