package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.Type;

public class DeInitialization extends Declaration {
    Type type;
    Identifier name;
    Operator equals;
    IntegerLiteral value;

    public DeInitialization(Type type, Identifier name, Operator equals, IntegerLiteral value) {
        this.type = type;
        this.name = name;
        this.equals = equals;
        this.value = value;
    }
}
