package package1.AST.OPERATIONS;

import package1.AST.Operation;
import java.util.Vector;

public abstract class OperationObjectVector extends Operation {

    // We expect to have object as Integer_literal, Boolean, identifier, MethodCall.
    private Vector<Object> objects;

    public OperationObjectVector(Vector<Object> objects) {
        this.objects = objects;
    }
}
