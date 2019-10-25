package package1.AST.METHODCALLS;

import package1.AST.Expression;
import package1.AST.MethodCall;
import package1.AST.ParameterList;
import package1.AST.Statement;
import package1.AST.TOKENS.Identifier;

public class IfStatement extends MethodCall {
    Expression expression;
    Statement statement;

    public IfStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }
}
