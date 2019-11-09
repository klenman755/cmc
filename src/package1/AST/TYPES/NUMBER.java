package package1.AST.TYPES;

import package1.AST.Terminal;
import package1.AST.Visitor;

public class NUMBER extends Terminal {

    public NUMBER( String spelling ) {
        this.spelling = spelling;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitNUMBER( this, arg );
    }
}
