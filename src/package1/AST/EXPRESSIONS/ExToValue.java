package package1.AST.EXPRESSIONS;

import package1.AST.Expression;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

public class ExToValue extends Expression {

    public Identifier name;
    public Operator doubleEquals;
    public IntegerLiteral value;

    public ExToValue(Identifier name, Operator doubleEquals, IntegerLiteral value) {
        this.name = name;
        this.doubleEquals = doubleEquals;
        this.value = value;
    }

    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitExToValue( this, arg );
    }
}
