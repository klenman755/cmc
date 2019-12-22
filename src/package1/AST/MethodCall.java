package package1.AST;

import package1.AST.TOKENS.Identifier;

public class MethodCall extends AST {
    public Identifier name;
    public ParameterList list;

    public MethodCall(Identifier name, ParameterList list) {
        this.name = name;
        this.list = list;
    }


     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitMethodCall( this, arg );
    }
}
