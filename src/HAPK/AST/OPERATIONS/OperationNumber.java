package HAPK.AST.OPERATIONS;

import HAPK.AST.Operation;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.TOKENS.Operator;
import HAPK.AST.Visitor;

import java.util.Vector;

public class OperationNumber extends Operation {

    public Identifier identifierOne;

    // First operator has to be '='
    public Vector<Operator> operators;

    // Checker will check the instances of values to determine if it is IntegerLiteral or Identifier
    // if Identifier fetch from the table the value (IntegerLiteral)
    public Vector<Object> values;

    public OperationNumber(Identifier identifierOne, Vector<Operator> operators, Vector<Object> values) {
        this.identifierOne = identifierOne;
        this.operators = operators;
        this.values = values;
    }
    @Override
     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitOperationNumbers( this, arg );
    }
}
