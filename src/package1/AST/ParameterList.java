package package1.AST;

import package1.AST.TOKENS.Identifier;

import java.util.Vector;

public class ParameterList extends AST {
    Vector<Identifier> list;

    public ParameterList(Vector<Identifier> list) {
        this.list = list;
    }
}