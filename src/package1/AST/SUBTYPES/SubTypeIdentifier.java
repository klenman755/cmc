package package1.AST.SUBTYPES;

import package1.AST.TOKENS.Operator;
import package1.AST.Type;

public class SubTypeIdentifier {

    Type type;
    Operator operator;

    public SubTypeIdentifier(Type type, Operator operator) {
        this.type = type;
        this.operator = operator;
    }
}
