package package1.AST.DECLARATIONS;

import package1.AST.*;
import package1.AST.TOKENS.Identifier;

public class DeArray extends Declaration {
    public VariableType type;
    public Identifier name;
    public ParameterList parameterList;
    public ValueList valueList;

    public DeArray(VariableType type, Identifier name, ParameterList parameterList, ValueList valueList) {
        this.type = type;
        this.name = name;
        this.parameterList = parameterList;
        this.valueList = valueList;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitDeArray( this, arg );
    }
}
