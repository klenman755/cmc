package package1.AST.METHODCALLS;

import package1.AST.MethodCall;
import package1.AST.ParameterList;
import package1.AST.TOKENS.Identifier;

public class Method extends MethodCall {
    Identifier name;
    ParameterList list;

    public Method(Identifier name, ParameterList list) {
        this.name = name;
        this.list = list;
    }
}
