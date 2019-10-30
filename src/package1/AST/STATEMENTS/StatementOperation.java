package package1.AST.STATEMENTS;

import package1.AST.Operation;
import package1.AST.Statement;

public class StatementOperation extends Statement {
    private Operation operation;

    public StatementOperation(Operation operation) {
        this.operation = operation;
    }
}
