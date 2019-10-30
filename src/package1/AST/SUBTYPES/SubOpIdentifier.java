package package1.AST.SUBTYPES;

import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;

public class SubOpIdentifier {

    IntegerLiteral literal;
    Operator operator;

    public SubOpIdentifier(IntegerLiteral literal, Operator operator) {
        this.literal = literal;
        this.operator = operator;
    }
}
