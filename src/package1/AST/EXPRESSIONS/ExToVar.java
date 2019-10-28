package package1.AST.EXPRESSIONS;

import package1.AST.Expression;
import package1.AST.Operation;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;

public class ExToVar extends Expression {

    Identifier name;
    Operator doubleEquals;
    Identifier variable;

    public ExToVar(Identifier name, Operator doubleEquals, Identifier variable) {
        this.name = name;
        this.doubleEquals = doubleEquals;
        this.variable = variable;
    }
}