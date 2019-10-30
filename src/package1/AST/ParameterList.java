package package1.AST;

import java.util.Vector;

public class ParameterList extends AST {
    private Vector<Identifier> identifiers;

    public ParameterList(Vector<Identifier> identifiers) {
        this.identifiers = identifiers;
    }
}
