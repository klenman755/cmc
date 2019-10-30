package package1.AST;

public class MethodCall extends AST {
    private Identifier identifier;
    private ParameterList parameterList;

    public MethodCall(Identifier identifier, ParameterList parameterList) {
        this.identifier = identifier;
        this.parameterList = parameterList;
    }
}
