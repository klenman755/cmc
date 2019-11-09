package package1.AST.TYPES;

import package1.AST.Terminal;
import package1.AST.Visitor;

public class BOO extends Terminal {

    public BOO( String spelling ) {
        this.spelling = spelling;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitBOO( this, arg );
    }

}
