package package1.AST.SUBTYPES;

import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.Operator;
import package1.AST.Visitor;

public class SubOpIntegerLiteral {

    public Identifier name;
    public Operator operator;

    public SubOpIntegerLiteral(Identifier name, Operator operator) {
        this.name = name;
        this.operator = operator;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitSubOpIntegerLiteral( this, arg );
    }
}
