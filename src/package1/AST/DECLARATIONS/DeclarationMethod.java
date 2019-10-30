package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.DeclarationList;
import package1.AST.Statement;
import package1.AST.Identifier;

import java.util.Vector;

public class DeclarationMethod extends Declaration {
    private Identifier name;
    private DeclarationList list;
    private Vector<Statement> statements;

    public DeclarationMethod(Identifier name, DeclarationList list, Vector<Statement> statements) {
        this.name = name;
        this.list = list;
        this.statements = statements;
    }
}
