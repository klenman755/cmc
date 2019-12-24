package HAPK.AST.TOKENS;

import HAPK.AST.Terminal;
import HAPK.AST.Visitor;

public class Identifier extends Terminal {

    public Identifier( String spelling )
    {
        this.spelling = spelling;
    }

    public Object visit( Visitor v, Object arg ) throws Exception {
        return v.visitIdentifier( this, arg );
    }
}
