package package1.AST.OPERATIONS;

import package1.AST.Operation;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

public class OpDeclerationFromVar extends Operation {

    Identifier identifierOne;
    Operator operator;
    Identifier identifierTwo;

    public OpDeclerationFromVar(Identifier identifierOne, Operator operator, Identifier identifierTwo) {
        this.identifierOne = identifierOne;
        this.operator = operator;
        this.identifierTwo = identifierTwo;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitOpDeclerationFromVar( this, arg );
    }
}
