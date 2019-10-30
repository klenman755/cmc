package package1.AST.OPERATIONS;

import package1.AST.Operation;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;

public class OpDeclerationFromVar extends Operation {

    Identifier identifierOne;
    Operator operator;
    Identifier identifierTwo;

    public OpDeclerationFromVar(Identifier identifierOne, Operator operator, Identifier identifierTwo) {
        this.identifierOne = identifierOne;
        this.operator = operator;
        this.identifierTwo = identifierTwo;
    }
}
