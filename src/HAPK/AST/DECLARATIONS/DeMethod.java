package HAPK.AST.DECLARATIONS;

import java.util.Vector;

import HAPK.AST.Declaration;
import HAPK.AST.DeclarationList;
import HAPK.AST.Statement;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.Visitor;
import HAPK.Address;

public class DeMethod extends Declaration {
    public Identifier name;
    public DeclarationList list;
    public Vector<Statement> statements;

    public Address address;

    public DeMethod(Identifier name, DeclarationList list, Vector<Statement> statements) {
        this.name = name;
        this.list = list;
        this.statements = statements;
    }

    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitDeMethod( this, arg );
    }
}
