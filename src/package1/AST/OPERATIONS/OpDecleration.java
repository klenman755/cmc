package package1.AST.OPERATIONS;

import package1.AST.Operation;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

import java.util.Vector;

public class OpDecleration extends Operation {

    public Identifier identifierOne;

    // First operator has to be '='
    public Vector<Operator> operators;

    // Checker will check the instances of values to determine if it is IntegerLiteral or Identifier
    // if Identifier fetch from the table the value (IntegerLiteral)
    public Vector<Object> values;

    public OpDecleration(Identifier identifierOne, Vector<Operator> operators, Vector<Object> values) {
        this.identifierOne = identifierOne;
        this.operators = operators;
        this.values = values;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitOpDecleration( this, arg );
    }
}
