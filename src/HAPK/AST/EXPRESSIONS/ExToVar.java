package HAPK.AST.EXPRESSIONS;

import HAPK.AST.Expression;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.TOKENS.Operator;
import HAPK.AST.Visitor;

public class ExToVar extends Expression {

    public Identifier name;
    public Operator doubleEquals;
    public Identifier variable;

    public ExToVar(Identifier name, Operator doubleEquals, Identifier variable) {
        this.name = name;
        this.doubleEquals = doubleEquals;
        this.variable = variable;
    }

     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitExToVar( this, arg );
    }

}
