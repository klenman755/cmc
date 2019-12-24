package HAPK.AST.EXPRESSIONS;

import HAPK.AST.Expression;
import HAPK.AST.TOKENS.BooValue;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.TOKENS.Operator;
import HAPK.AST.Visitor;

public class ExToBoo extends Expression {

    public Identifier name;
    public Operator doubleEquals;
    public BooValue boo;

    public ExToBoo(Identifier name, Operator doubleEquals, BooValue boo) {
        this.name = name;
        this.doubleEquals = doubleEquals;
        this.boo = boo;
    }

    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitExToBoo( this, arg );
    }
}
