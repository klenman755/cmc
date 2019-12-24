package HAPK.AST.EVALUATION_BLOCKS;

import HAPK.AST.EvaluationBlock;
import HAPK.AST.Expression;
import HAPK.AST.Statement;
import HAPK.AST.Visitor;

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
