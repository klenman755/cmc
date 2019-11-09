package package1;

import package1.AST.*;
import package1.AST.DECLARATIONS.DeArray;
import package1.AST.DECLARATIONS.DeInitialization;
import package1.AST.DECLARATIONS.DeMethod;
import package1.AST.DECLARATIONS.DeVariable;
import package1.AST.EXPRESSIONS.ExToBoo;
import package1.AST.EXPRESSIONS.ExToValue;
import package1.AST.EXPRESSIONS.ExToVar;
import package1.AST.METHODCALLS.IfStatement;
import package1.AST.METHODCALLS.Method;
import package1.AST.METHODCALLS.WhileStatement;
import package1.AST.OPERATIONS.OpDeclerationBoo;
import package1.AST.OPERATIONS.OpDeclerationNum;
import package1.AST.OPERATIONS.OpDeclerationFromVar;
import package1.AST.OPERATIONS.OpDeclerationVarAndLiteral;
import package1.AST.SUBTYPES.SubOpIdentifier;
import package1.AST.SUBTYPES.SubOpIntegerLiteral;
import package1.AST.SUBTYPES.SubTypeIdentifier;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.TYPES.BOO;
import package1.AST.TYPES.NUMBER;

public class Checker implements Visitor
{
	private IdentificationTable idTable = new IdentificationTable();

	public void check( Program p )
	{
		p.visit( this, null );
	}

	public Object visitProgram( Program p, Object arg )
	{
		idTable.openScope();

		p.block.visit( this, null );

		idTable.closeScope();

		return null;
	}

	public Object visitBlock(Block b, Object arg )
	{
		b.decs.visit( this, null );
		b.stats.visit( this, null );

		return null;
	}

	public Object visitVariableType(VariableType variableType, Object arg) {
		variableType.boo.visit( this, null );
		variableType.number.visit( this, null );

		return null;
	}

	public Object visitStatement(Statement s, Object arg) {
		s.operation.visit( this, null );
		s.methodCall.visit( this, null );

		return null;
	}

	public Object visitOpDeclerationBoo(OpDeclerationBoo opDeclerationBoo, Object arg) {
		opDeclerationBoo.identifier.visit( this, null );
		opDeclerationBoo.operator.visit( this, null );
		opDeclerationBoo.boo.visit( this, null );

		return null;
	}

	public Object visitExToBoo(ExToBoo exToBoo, Object arg) {
		exToBoo.name.visit( this, null );
		exToBoo.doubleEquals.visit( this, null );
		exToBoo.boo.visit( this, null );

		return null;
	}

	public Object visitExToValue(ExToValue exToValue, Object arg) {
		exToValue.name.visit( this, null );
		exToValue.doubleEquals.visit( this, null );
		exToValue.value.visit( this, null );

		return null;
	}

	public Object visitExToVar(ExToVar exToVar, Object arg) {
		exToVar.name.visit( this, null );
		exToVar.doubleEquals.visit( this, null );
		exToVar.variable.visit( this, null );

		return null;
	}

	public Object visitIfStatement(IfStatement ifStatement, Object arg) {
		ifStatement.expression.visit( this, null );
		ifStatement.statement.visit( this, null );
		return null;
	}

	public Object visitMethod(Method method, Object arg) {
		method.name.visit( this, null );
		method.list.visit( this, null );

		return null;
	}

	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) {
		whileStatement.expression.visit( this, null );
		whileStatement.statement.visit( this, null );

		return null;
	}

	public Object visitOpDecleration(OpDeclerationNum opDecleration, Object arg) {
		opDecleration.identifier.visit( this, null );
		opDecleration.operator.visit( this, null );
		opDecleration.integerLiteral.visit( this, null );
		return null;
	}

	public Object visitOpDeclerationFromVar(OpDeclerationFromVar opDeclerationFromVar, Object arg) {
		opDeclerationFromVar.identifierOne.visit( this, null );
		opDeclerationFromVar.operator.visit( this, null );
		opDeclerationFromVar.identifierTwo.visit( this, null );

		return null;
	}

	public Object visitSubOpIdentifier(SubOpIdentifier subOpIdentifier, Object arg) {
		subOpIdentifier.literal.visit( this, null );
		subOpIdentifier.operator.visit( this, null );
		return null;
	}

	public Object visitSubOpIntegerLiteral(SubOpIntegerLiteral subOpIntegerLiteral, Object arg) {
		subOpIntegerLiteral.name.visit( this, null );
		subOpIntegerLiteral.operator.visit( this, null );

		return null;
	}

	public Object visitSubTypeIdentifier(SubTypeIdentifier subTypeIdentifier, Object arg) {
		subTypeIdentifier.type.visit( this, null );
		subTypeIdentifier.operator.visit( this, null );

		return null;
	}

	//
	//
	//





	public Object visitValueList(ValueList valueList, Object arg) {
		for( IntegerLiteral integer : valueList.numbers )
			integer.visit( this, null );

		for( BooValue boo : valueList.booleans )
			boo.visit( this, null );

		//TODO JAN ADDS TO TYPE AND RETURNS TYPE?? - WTF
		return null;
	}


	public Object visitDeclarationList(DeclarationList dl, Object arg) {

		for( SubTypeIdentifier subTypeIdentifier : dl.declarationList )
			subTypeIdentifier.visit( this, null );

		return null;
	}

	public Object visitParameterList(ParameterList pl, Object arg) {

		for( Identifier identifier : pl.list )
			identifier.visit( this, null );

		return null;
	}

	public Object visitOpDeclerationVarAndLiteral(OpDeclerationVarAndLiteral opDeclerationVarAndLiteral, Object arg) {
		opDeclerationVarAndLiteral.name.visit( this, null );

		for( SubOpIdentifier v : opDeclerationVarAndLiteral.variables )
			v.visit( this, null );

		for( SubOpIntegerLiteral v : opDeclerationVarAndLiteral.literals )
			v.visit( this, null );

		return null;
	}




	//
	// DECLARATIONS
	//

	public Object visitDeMethod(DeMethod deMethod, Object arg) {
		String id = (String) deMethod.name.visit( this, null );
		deMethod.list.visit( this, null );
		deMethod.statement.visit( this, null );

		idTable.enter(id, deMethod);

		return null;
	}

	public Object visitDeInitialization(DeInitialization deInitialization, Object arg) {
		deInitialization.type.visit( this, null );
		String id = (String) deInitialization.name.visit( this, null );
		deInitialization.equals.visit( this, null );
		deInitialization.value.visit( this, null );

		// TODO Example of declaration
		idTable.enter(id, deInitialization);

		return null;
	}

	public Object visitDeArray(DeArray deArray, Object arg) {
		deArray.type.visit(this, null);
		String id = (String) deArray.name.visit(this, null);
		deArray.parameterList.visit(this, null);
		deArray.valueList.visit(this, null);

		idTable.enter(id, deArray);

		return null;
	}

	public Object visitDeVariable(DeVariable deVariable, Object arg) {
		deVariable.type.visit( this, null );
		String id = (String)  deVariable.name.visit( this, null );

		idTable.enter(id, deVariable);

		return null;
	}

	//
	//
	//




	public Object visitIdentifier( Identifier i, Object arg ) {
		return i.spelling;
	}

	public Object visitIntegerLiteral( IntegerLiteral i, Object arg ) {
		return i.spelling;
	}

	public Object visitOperator( Operator o, Object arg ) {
		return o.spelling;
	}

	public Object visitBooValue(BooValue booValue, Object arg) {
		return booValue.spelling;
	}

	public Object visitBOO(BOO boo, Object arg) {
	return boo.spelling;
	}

	public Object visitNUMBER(NUMBER number, Object arg) {
		return number.spelling;
	}
}