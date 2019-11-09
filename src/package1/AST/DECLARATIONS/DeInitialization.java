package package1.AST.DECLARATIONS;

import package1.AST.Declaration;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.VariableType;
import package1.AST.Visitor;

public class DeInitialization extends Declaration {
    public VariableType type;
    public Identifier name;
    public Operator equals;
    public IntegerLiteral value;

    public DeInitialization(VariableType type, Identifier name, Operator equals, IntegerLiteral value) {
        this.type = type;
        this.name = name;
        this.equals = equals;
        this.value = value;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitDeInitialization( this, arg );
    }
}
