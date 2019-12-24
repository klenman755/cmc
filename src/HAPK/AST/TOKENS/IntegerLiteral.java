package HAPK.AST.TOKENS;

import HAPK.AST.Terminal;
import HAPK.AST.Visitor;

public class IntegerLiteral extends Terminal {

    public IntegerLiteral( String spelling ) {
        this.spelling = spelling;
    }

    public Object visit(Visitor v, Object arg ) throws Exception {
        return v.visitIntegerLiteral( this, arg );
    }
}

