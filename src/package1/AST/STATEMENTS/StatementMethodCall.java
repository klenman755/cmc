package package1.AST.STATEMENTS;

import package1.AST.MethodCall;
import package1.AST.Statement;

public class StatementMethodCall extends Statement {
    private MethodCall methodCall;

    public StatementMethodCall(MethodCall methodCall) {
        this.methodCall = methodCall;
    }
}
