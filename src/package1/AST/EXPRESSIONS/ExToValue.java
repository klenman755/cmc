package package1.AST.EXPRESSIONS;

import package1.AST.Expression;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;

public class ExToValue extends Expression {

    Identifier name;
    Operator doubleEquals;
    IntegerLiteral value;

    public ExToValue(Identifier name, Operator doubleEquals, IntegerLiteral value) {
        this.name = name;
        this.doubleEquals = doubleEquals;
        this.value = value;
    }
}
