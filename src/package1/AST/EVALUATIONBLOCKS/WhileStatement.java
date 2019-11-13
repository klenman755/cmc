package package1.AST.EVALUATIONBLOCKS;

import package1.AST.EvaluationBlock;
import package1.AST.Expression;
import package1.AST.MethodCall;
import package1.AST.Statement;
import package1.AST.Visitor;

public class WhileStatement extends EvaluationBlock {
    public Expression expression;
    public Statement statement;

    public WhileStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitWhileStatement( this, arg );
    }
}
