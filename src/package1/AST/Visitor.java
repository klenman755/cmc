/*
 * 27.09.2016 Minor edit
 * 29.10.2009 New package structure
 * 22.10.2006 Original version (based in Watt&Brown)
 */
 
package package1.AST;


import package1.AST.DECLARATIONS.DeInitialization;
import package1.AST.DECLARATIONS.DeMethod;
import package1.AST.DECLARATIONS.DeVariable;
import package1.AST.EXPRESSIONS.ExToBoo;
import package1.AST.EXPRESSIONS.ExToValue;
import package1.AST.EXPRESSIONS.ExToVar;
import package1.AST.METHODCALLS.IfStatement;
import package1.AST.METHODCALLS.Method;
import package1.AST.METHODCALLS.WhileStatement;
import package1.AST.OPERATIONS.OpDecleration;
import package1.AST.OPERATIONS.OpDeclerationFromVar;
import package1.AST.OPERATIONS.OpDeclerationVarAndLiteral;
import package1.AST.OPERATIONS.OpMethodCall;
import package1.AST.SUBTYPES.SubOpIdentifier;
import package1.AST.SUBTYPES.SubOpIntegerLiteral;
import package1.AST.SUBTYPES.SubTypeIdentifier;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;

public interface Visitor
{
	//TODO can be removed?
//	public Object visitExpression(Expression e, Object arg);
//
//	public Object visitMethodCall(MethodCall mc, Object arg);
//
//	public Object visitOperation(Operation o, Object arg);
//
//	public Object visitDeclaration(Declaration d, Object arg);
//
//	public Object visitVariableType(VariableType vt, Object arg);

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

	public Object visitMethod(Method method, Object arg);

	public Object visitWhileStatement(WhileStatement whileStatement, Object arg);

	public Object visitOpDecleration(OpDecleration opDecleration, Object arg);

	public Object visitOpDeclerationFromVar(OpDeclerationFromVar opDeclerationFromVar, Object arg);

	public Object visitOpDeclerationVarAndLiteral(OpDeclerationVarAndLiteral opDeclerationVarAndLiteral, Object arg);

	public Object visitOpMethodCall(OpMethodCall opMethodCall, Object arg);

	public Object visitSubOpIdentifier(SubOpIdentifier subOpIdentifier, Object arg);

	public Object visitSubOpIntegerLiteral(SubOpIntegerLiteral subOpIntegerLiteral, Object arg);

	public Object visitSubTypeIdentifier(SubTypeIdentifier subTypeIdentifier, Object arg);

	public Object visitProgram(Program program, Object arg);
}