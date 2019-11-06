package package1.AST;

import package1.AST.TOKENS.Identifier;

import java.util.Vector;

public class ParameterList extends AST {
    public Vector<Identifier> list = new Vector<Identifier>();

    public Object visit(Visitor v, Object arg) {
        return v.visitParameterList( this, arg );
    }
}
