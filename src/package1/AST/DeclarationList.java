package package1.AST;

import package1.AST.SUBTYPES.SubTypeIdentifier;

import java.util.Vector;

public class DeclarationList extends AST {
    private  Vector<Identifier> identifiers;

    public DeclarationList(Vector<Identifier> identifiers) {
        this.identifiers = identifiers;
    }
}
