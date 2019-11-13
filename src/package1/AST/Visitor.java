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
import package1.AST.EVALUATIONBLOCKS.IfStatement;
import package1.AST.EVALUATIONBLOCKS.WhileStatement;
import package1.AST.EXPRESSIONS.ExToBoo;
import package1.AST.EXPRESSIONS.ExToValue;
import package1.AST.EXPRESSIONS.ExToVar;
import package1.AST.METHODCALLS.Method;
import package1.AST.OPERATIONS.*;
import package1.AST.SUBTYPES.SubOpIdentifier;
import package1.AST.SUBTYPES.SubOpIntegerLiteral;
import package1.AST.SUBTYPES.SubTypeIdentifier;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.TYPES.BOO;
import package1.AST.TYPES.NUMBER;

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

	public Object visitOpDecleration(OpDeclerationNum opDecleration, Object arg);

	public Object visitOpDeclerationFromVar(OpDeclerationFromVar opDeclerationFromVar, Object arg);

	public Object visitOpDeclerationVarAndLiteral(OpDeclerationVarAndLiteral opDeclerationVarAndLiteral, Object arg);

	public Object visitSubOpIdentifier(SubOpIdentifier subOpIdentifier, Object arg);

	public Object visitSubOpIntegerLiteral(SubOpIntegerLiteral subOpIntegerLiteral, Object arg);

	public Object visitSubTypeIdentifier(SubTypeIdentifier subTypeIdentifier, Object arg);

	public Object visitProgram(Program program, Object arg);

	public Object visitDeArray(DeArray deArray, Object arg);

	public Object visitBooValue(BooValue booValue, Object arg);

	public Object visitValueList(ValueList valueList, Object arg);

	public Object visitVariableType(VariableType variableType, Object arg);

	public Object visitBOO(BOO boo, Object arg);

	public Object visitNUMBER(NUMBER number, Object arg);

	public Object visitOpDeclerationBoo(OpDeclerationBoo opDeclerationBoo, Object arg);
	
	public Object visitMethodCall(MethodCall methodCall, Object arg);
}