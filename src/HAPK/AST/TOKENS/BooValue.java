package HAPK.AST.TOKENS;

import HAPK.AST.Terminal;
import HAPK.AST.Visitor;

public class BooValue extends Terminal {

    public BooValue(String spelling) {
        this.spelling = spelling;
    }

    public Object visit(Visitor v, Object arg ) throws Exception {
        return v.visitBooValue( this, arg );
    }
}

