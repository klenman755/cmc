/*
 * 27.09.2016 Minor edit
 * 29.10.2009 New package structure
 * 22.10.2006 Original version (based in Watt&Brown)
 */
 
package package1.AST;


import package1.AST.DECLARATIONS.DeArray;
import package1.AST.DECLARATIONS.DeInitialization;
import package1.AST.DECLARATIONS.DeMethod;
import package1.AST.DECLARATIONS.DeVariable;
import package1.AST.EVALUATION_BLOCKS.IfStatement;
import package1.AST.EVALUATION_BLOCKS.WhileStatement;
import package1.AST.EXPRESSIONS.ExToBoo;
import package1.AST.EXPRESSIONS.ExToValue;
import package1.AST.EXPRESSIONS.ExToVar;
import package1.AST.OPERATIONS.*;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.VALUE_LISTS.ValueListBooValue;
import package1.AST.VALUE_LISTS.ValueListIntegerLiteralValue;

public interface Visitor
{
	public Object visitBlock(Block b, Object arg);

	public Object visitDeclarationList( DeclarationList dl, Object arg);

	public Object visitParameterList(ParameterList pl, Object arg);

	public Object visitStatement(Statement s, Object arg);

	public Object visitIdentifier(Identifier i, Object arg);

	public Object visitIntegerLiteral(IntegerLiteral i, Object arg);

	public Object visitOperator(Operator o, Object arg);

	public Object visitDeMethod(DeMethod deMethod, Object arg);

	public Object visitDeInitialization(DeInitialization deInitialization, Object arg);

	public Object visitDeVariable(DeVariable deVariable, Object arg);

	public Object visitExToBoo(ExToBoo exToBoo, Object arg);

	public Object visitExToValue(ExToValue exToValue, Object arg);

	public Object visitExToVar(ExToVar exToVar, Object arg);

	public Object visitIfStatement(IfStatement ifStatement, Object arg);

	public Object visitWhileStatement(WhileStatement whileStatement, Object arg);

	public Object visitOpDecleration(OpDecleration opDeclerationFromVar, Object arg);

	public Object visitProgram(Program program, Object arg);

	public Object visitDeArray(DeArray deArray, Object arg);

	public Object visitBooValue(BooValue booValue, Object arg);

	public Object visitValueList(ValueList valueList, Object arg);

	public Object visitVariableType(VariableType variableType, Object arg);

	public Object visitOpDeclerationBoo(OpDeclerationBoo opDeclerationBoo, Object arg);
	
	public Object visitMethodCall(MethodCall methodCall, Object arg);

	public Object visitValueListBooValue(ValueListBooValue valueListBooValue, Object arg);

	public Object visitValueListIntegerLiteralValue(ValueListIntegerLiteralValue valueListIntegerLiteralValue,
			Object arg);

	public Object visitDeclaration(Declaration declaration, Object arg);
}