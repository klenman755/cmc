package package1.AST.OPERATIONS;

import package1.AST.Operation;
import package1.AST.TOKENS.Operator;

public abstract class OperationObject extends Operation {

    // We expect to have object as Integer_literal, Boolean, identifier, MethodCall.
    private Object object;

    public OperationObject(Object object) {
        this.object = object;
    }
}
