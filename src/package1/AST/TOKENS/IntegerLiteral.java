package package1.AST.TOKENS;

import package1.AST.Terminal;
import package1.AST.Visitor;

public class IntegerLiteral extends Terminal {

    public IntegerLiteral( String spelling ) {
        this.spelling = spelling;
    }

    public Object visit(Visitor v, Object arg ) {
        return v.visitIntegerLiteral( this, arg );
    }
}

