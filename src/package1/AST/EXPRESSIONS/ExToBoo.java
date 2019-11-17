package package1.AST.EXPRESSIONS;

import package1.AST.Expression;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

public class ExToBoo extends Expression {

    public Identifier name;
    public Operator doubleEquals;
    public BooValue boo;

    public ExToBoo(Identifier name, Operator doubleEquals, BooValue boo) {
        this.name = name;
        this.doubleEquals = doubleEquals;
        this.boo = boo;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitExToBoo( this, arg );
    }
}
