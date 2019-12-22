package package1.AST.EVALUATION_BLOCKS;

import package1.AST.*;
import package1.AST.TOKENS.Identifier;

import java.util.Vector;

public class IfStatement extends EvaluationBlock {
    public Expression expression;
    public Vector<Statement> statements;

    public IfStatement(Expression expression, Vector<Statement> statements) {
        this.expression = expression;
        this.statements = statements;
    }

     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitIfStatement( this, arg );
    }
}
