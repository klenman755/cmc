package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.VariableType;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;


//TODO checker needs to check instance of value
public class DeInitialization extends Declaration {
    public VariableType type;
    public Identifier name;
    public Operator equals;
    public Object value;

    public DeInitialization(VariableType type, Identifier name, Operator equals, Object value) {
        this.type = type;
        this.name = name;
        this.equals = equals;
        this.value = value;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitDeInitialization( this, arg );
    }
}
