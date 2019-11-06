package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.TOKENS.Identifier;
import package1.AST.VariableType;
import package1.AST.Visitor;

public class DeVariable extends Declaration {
    VariableType type;
    Identifier name;

    public DeVariable(VariableType type, Identifier name) {
        this.type = type;
        this.name = name;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitDeVariable( this, arg );
    }

}
