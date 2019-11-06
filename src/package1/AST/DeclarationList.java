package package1.AST;

import package1.AST.SUBTYPES.SubTypeIdentifier;
import java.util.Vector;

public class DeclarationList extends AST {
    public Vector<SubTypeIdentifier> declarationList = new Vector<SubTypeIdentifier>();

    public DeclarationList(Vector<SubTypeIdentifier> declarationList) {
        this.declarationList = declarationList;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitDeclarationList( this, arg );
    }
}
