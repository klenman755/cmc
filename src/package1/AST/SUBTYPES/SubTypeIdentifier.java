package package1.AST.SUBTYPES;

import package1.AST.TOKENS.Operator;
import package1.AST.VariableType;
import package1.AST.Visitor;

public class SubTypeIdentifier {

    public VariableType type;
    public Operator operator;

    public SubTypeIdentifier(VariableType type, Operator operator) {
        this.type = type;
        this.operator = operator;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitSubTypeIdentifier
                ( this, arg );
    }
}
