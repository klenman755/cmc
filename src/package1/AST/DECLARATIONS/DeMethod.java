package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.DeclarationList;
import package1.AST.Statement;
import package1.AST.TOKENS.Identifier;
import package1.AST.Visitor;

public class DeMethod extends Declaration {
    Identifier name;
    DeclarationList list;
    Statement statement;

    public DeMethod(Identifier name, DeclarationList list, Statement statement) {
        this.name = name;
        this.list = list;
        this.statement = statement;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitDeMethod( this, arg );
    }
}
