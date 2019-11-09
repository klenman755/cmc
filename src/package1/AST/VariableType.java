package package1.AST;

import package1.AST.TYPES.BOO;
import package1.AST.TYPES.NUMBER;

public abstract class VariableType extends AST {

    public BOO boo;
    public NUMBER number;

    public VariableType(BOO boo, NUMBER number) {
        this.boo = boo;
        this.number = number;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitVariableType( this, arg );
    }
}
