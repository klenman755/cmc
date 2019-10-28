package package1.AST.SUBTYPES;

import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;

public class SubOpIntegerLiteral {

    Identifier name;
    Operator operator;

    public SubOpIntegerLiteral(Identifier name, Operator operator) {
        this.name = name;
        this.operator = operator;
    }
}
