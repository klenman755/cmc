package package1.AST.EXPRESSIONS;

import package1.AST.Expression;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;
import package1.AST.TYPES.BOO;

public class ExToBoo extends Expression {

    Identifier name;
    Operator doubleEquals;
    BOO boo;

    public ExToBoo(Identifier name, Operator doubleEquals, BOO boo) {
        this.name = name;
        this.doubleEquals = doubleEquals;
        this.boo = boo;
    }
}
