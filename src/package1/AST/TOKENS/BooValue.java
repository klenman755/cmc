package package1.AST.TOKENS;

import package1.AST.Terminal;
import package1.AST.Visitor;

public class BooValue extends Terminal {

    public BooValue(String spelling) {
        this.spelling = spelling;
    }

    public Object visit(Visitor v, Object arg ) {
        return v.visitBooValue( this, arg );
    }
}

