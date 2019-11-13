package package1.AST.EVALUATIONBLOCKS;

import package1.AST.*;
import package1.AST.TOKENS.Identifier;

public class IfStatement extends EvaluationBlock {
    public Expression expression;
    public Statement statement;

    public IfStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitIfStatement( this, arg );
    }
}
