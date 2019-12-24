package HAPK.AST.DECLARATIONS;

import HAPK.AST.Declaration;
import HAPK.AST.VariableType;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.TOKENS.Operator;
import HAPK.AST.Visitor;
import HAPK.Address;

public class DeVariable extends Declaration {
    public VariableType type;
    public Identifier name;
    public Operator equalsSign;
    public Object value;

    public Address address;

    public DeVariable(VariableType type, Identifier name, Operator equalsSign, Object value) {
        this.type = type;
        this.name = name;
        this.equalsSign = equalsSign;
        this.value = value;
    }

     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitDeInitialization( this, arg );
    }
}
