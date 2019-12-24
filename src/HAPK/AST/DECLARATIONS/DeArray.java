package HAPK.AST.DECLARATIONS;

import HAPK.AST.*;
import HAPK.AST.TOKENS.Identifier;
import HAPK.Address;

public class DeArray extends Declaration {
    public VariableType type;
    public Identifier name;
    public ParameterList parameterList;

    public Address address;

    public DeArray(VariableType type, Identifier name, ParameterList parameterList) {
        this.type = type;
        this.name = name;
        this.parameterList = parameterList;
    }

    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitDeArray( this, arg );
    }
}
