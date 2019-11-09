package package1.AST.OPERATIONS;

import package1.AST.Operation;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

public class OpDeclerationBoo extends Operation {

    public Identifier identifier;
    public Operator operator;
    public BooValue boo;

    public OpDeclerationBoo(Identifier identifier, Operator operator, BooValue boo) {
        this.identifier = identifier;
        this.operator = operator;
        this.boo = boo;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitOpDeclerationBoo( this, arg );
    }
}
