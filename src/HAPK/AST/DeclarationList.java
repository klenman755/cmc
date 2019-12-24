package HAPK.AST;

import HAPK.AST.TOKENS.Identifier;

import java.util.Vector;

public class DeclarationList extends AST {
    public Vector<VariableType> variableTypeList = new Vector<VariableType>();
    public Vector<Identifier> identifierList = new Vector<Identifier>();

    public DeclarationList(Vector<VariableType> variableTypeList, Vector<Identifier> identifierList) {
        this.variableTypeList = variableTypeList;
        this.identifierList = identifierList;
    }

    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitDeclarationList(this, arg);
    }
}
