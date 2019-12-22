package package1.AST;

import package1.AST.DECLARATIONS.DeArray;
import package1.AST.DECLARATIONS.DeVariable;
import package1.AST.DECLARATIONS.DeMethod;
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

public interface Visitor
{
	public Object visitBlock(Block b, Object arg) throws Exception;

	public Object visitDeclarationList( DeclarationList dl, Object arg) throws Exception;

	public Object visitParameterList(ParameterList pl, Object arg) throws Exception;

	public Object visitStatement(Statement s, Object arg) throws Exception;

	public Object visitIdentifier(Identifier i, Object arg) throws Exception;

	public Object visitIntegerLiteral(IntegerLiteral i, Object arg) throws Exception;

	public Object visitOperator(Operator o, Object arg) throws Exception;

	public Object visitDeMethod(DeMethod deMethod, Object arg) throws Exception;

	public Object visitDeInitialization(DeVariable deInitialization, Object arg) throws Exception;

	public Object visitExToBoo(ExToBoo exToBoo, Object arg) throws Exception;

	public Object visitExToValue(ExToValue exToValue, Object arg) throws Exception;

	public Object visitExToVar(ExToVar exToVar, Object arg) throws Exception;

	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception;

	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception;

	public Object visitProgram(Program program, Object arg) throws Exception;

	public Object visitDeArray(DeArray deArray, Object arg) throws Exception;

	public Object visitBooValue(BooValue booValue, Object arg) throws Exception;

	public Object visitVariableType(VariableType variableType, Object arg) throws Exception;

	public Object visitMethodCall(MethodCall methodCall, Object arg) throws Exception;

	public Object visitOperationNumbers(OperationNumber operationNumbers, Object arg) throws Exception;

	public Object visitOperationBoo(OperationBoo operationBoo, Object arg) throws Exception;

}
