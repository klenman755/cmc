package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.VariableType;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;
import package1.Address;

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
