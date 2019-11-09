package package1.AST;

import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.IntegerLiteral;

import java.util.Vector;

public class ValueList extends AST {
    public Vector<IntegerLiteral> numbers = new Vector<IntegerLiteral>();
    public Vector<BooValue> booleans = new Vector<BooValue>();

    public Object visit(Visitor v, Object arg) {
        return v.visitValueList( this, arg );
    }
}
