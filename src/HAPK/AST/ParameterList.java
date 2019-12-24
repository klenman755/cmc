package HAPK.AST;

import java.util.Vector;

public class ParameterList extends AST {
    public Vector<Object> list = new Vector<Object>();

    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitParameterList(this, arg);
    }
}
