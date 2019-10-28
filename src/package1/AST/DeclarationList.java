package package1.AST;

import package1.AST.SUBTYPES.SubTypeIdentifier;
import package1.AST.TOKENS.Identifier;

import java.util.Vector;

public class DeclarationList extends AST {
    Vector<SubTypeIdentifier> declarationList;

    public DeclarationList(Vector<SubTypeIdentifier> declarationList) {
        this.declarationList = declarationList;
    }
}
