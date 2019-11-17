package package1.AST.DECLARATIONS;

import java.util.Vector;

import package1.AST.Declaration;
import package1.AST.DeclarationList;
import package1.AST.Statement;
import package1.AST.TOKENS.Identifier;
import package1.AST.Visitor;

public class DeMethod extends Declaration {
    public Identifier name;
    public DeclarationList list;
    public Vector<Statement> statements;

    public DeMethod(Identifier name, DeclarationList list, Vector<Statement> statements) {
        this.name = name;
        this.list = list;
        this.statements = statements;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitDeMethod( this, arg );
    }
}
