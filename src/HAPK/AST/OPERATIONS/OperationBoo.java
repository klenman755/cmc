package HAPK.AST.OPERATIONS;

import HAPK.AST.Operation;
import HAPK.AST.TOKENS.BooValue;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.TOKENS.Operator;
import HAPK.AST.Visitor;

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
