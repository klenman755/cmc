package package1.AST.OPERATIONS;

import package1.AST.Operation;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

public class OperationBoo extends Operation {

    public Identifier identifier;
    public Operator operator;
    public BooValue boo;

    public OperationBoo(Identifier identifier, Operator operator, BooValue boo) {
        this.identifier = identifier;
        this.operator = operator;
        this.boo = boo;
    }

    @Override
     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitOperationBoo( this, arg );
    }
}
