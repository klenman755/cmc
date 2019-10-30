package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.Identifier;
import package1.AST.TOKENS.Operator;

public class DeclarationInitialization extends Declaration {
    Identifier name;
    Operator equals;
    Object value;

    public DeclarationInitialization(Identifier name, Operator equals, Object value) {
        this.name = name;
        this.equals = equals;
        this.value = value;
    }
}
