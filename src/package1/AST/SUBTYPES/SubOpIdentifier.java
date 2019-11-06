package package1.AST.SUBTYPES;

import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

public class SubOpIdentifier {

    IntegerLiteral literal;
    Operator operator;

    public SubOpIdentifier(IntegerLiteral literal, Operator operator) {
        this.literal = literal;
        this.operator = operator;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitSubOpIdentifier( this, arg );
    }
}
