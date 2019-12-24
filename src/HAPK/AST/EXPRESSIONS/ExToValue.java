package HAPK.AST.EXPRESSIONS;

import HAPK.AST.Expression;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.TOKENS.IntegerLiteral;
import HAPK.AST.TOKENS.Operator;
import HAPK.AST.Visitor;

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
