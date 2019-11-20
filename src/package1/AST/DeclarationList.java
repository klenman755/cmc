package package1.AST;

import java.util.Vector;

import package1.AST.TOKENS.Identifier;

public class DeclarationList extends AST {
    public Vector<VariableType> variableTypeList = new Vector<VariableType>();
    public Vector<Identifier> identifierList = new Vector<Identifier>();

    public DeclarationList(Vector<VariableType> variableTypeList, Vector<Identifier> identifierList) {
		this.variableTypeList = variableTypeList;
		this.identifierList = identifierList;
	}

	public Object visit(Visitor v, Object arg) {
        return v.visitDeclarationList( this, arg );
    }
}
