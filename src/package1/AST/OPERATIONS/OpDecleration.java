package package1.AST.OPERATIONS;

import package1.AST.Operation;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

public class OpDecleration extends Operation {

    Identifier identifier;
    Operator operator;
    IntegerLiteral integerLiteral;

    public OpDecleration(Identifier identifier, Operator operator, IntegerLiteral integerLiteral) {
        this.identifier = identifier;
        this.operator = operator;
        this.integerLiteral = integerLiteral;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitOpDecleration( this, arg );
    }
}
