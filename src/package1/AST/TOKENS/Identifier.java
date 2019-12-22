package package1.AST.TOKENS;

import package1.AST.Terminal;
import package1.AST.Visitor;

public class Identifier extends Terminal {

    public Identifier( String spelling )
    {
        this.spelling = spelling;
    }

    public Object visit( Visitor v, Object arg ) throws Exception {
        return v.visitIdentifier( this, arg );
    }
}
