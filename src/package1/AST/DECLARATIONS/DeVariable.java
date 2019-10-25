package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.TOKENS.Identifier;
import package1.AST.Type;

public class DeVariable extends Declaration {
    Type type;
    Identifier name;

    public DeVariable(Type type, Identifier name) {
        this.type = type;
        this.name = name;
    }
}
