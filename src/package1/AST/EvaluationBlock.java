package package1.AST;

import java.util.Vector;

public class EvaluationBlock extends AST {
    private Expression expression;
    private Vector<Statement> statements;

    public EvaluationBlock(Expression expression, Vector<Statement> statements) {
        this.expression = expression;
        this.statements = statements;
    }
}
