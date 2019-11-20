package package1.AST;

import package1.AST.TOKENS.Identifier;

import java.util.Vector;

public class ParameterList extends AST {
    public Vector<Object> list = new Vector<Object>();

    public Object visit(Visitor v, Object arg) {
        return v.visitParameterList( this, arg );
    }
}
