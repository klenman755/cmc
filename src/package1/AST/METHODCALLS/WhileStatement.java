package package1.AST.METHODCALLS;

import package1.AST.Expression;
import package1.AST.MethodCall;
import package1.AST.Statement;
import package1.AST.Visitor;

public class WhileStatement extends MethodCall {
    Expression expression;
    Statement statement;

    public WhileStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitWhileStatement( this, arg );
    }
}
