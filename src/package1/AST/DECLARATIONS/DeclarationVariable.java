package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.Identifier;

public class DeclarationVariable extends Declaration {
    Identifier name;

    public DeclarationVariable(Identifier name) {
        this.name = name;
    }
}
