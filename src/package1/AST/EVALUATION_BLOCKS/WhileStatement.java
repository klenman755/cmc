package package1.AST.EVALUATION_BLOCKS;

import package1.AST.EvaluationBlock;
import package1.AST.Expression;
import package1.AST.Statement;
import package1.AST.Visitor;

import java.util.Vector;

public class WhileStatement extends EvaluationBlock {
    public Expression expression;
    public Vector<Statement> statements;

    public WhileStatement(Expression expression, Vector<Statement> statements) {
        this.expression = expression;
        this.statements = statements;
    }

     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitWhileStatement( this, arg );
    }
}
