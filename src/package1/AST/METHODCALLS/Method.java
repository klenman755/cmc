package package1.AST.METHODCALLS;

import package1.AST.MethodCall;
import package1.AST.ParameterList;
import package1.AST.TOKENS.Identifier;
import package1.AST.Visitor;

public class Method extends MethodCall {
    public Identifier name;
    public ParameterList list;

    public Method(Identifier name, ParameterList list) {
        this.name = name;
        this.list = list;
    }


    public Object visit(Visitor v, Object arg) {
        return v.visitMethod( this, arg );
    }
}
