package HAPK.AST;

import HAPK.AST.DECLARATIONS.DeArray;
import HAPK.AST.DECLARATIONS.DeMethod;
import HAPK.AST.DECLARATIONS.DeVariable;
import HAPK.AST.EVALUATION_BLOCKS.IfStatement;
import HAPK.AST.EVALUATION_BLOCKS.WhileStatement;
import HAPK.AST.EXPRESSIONS.ExToBoo;
import HAPK.AST.EXPRESSIONS.ExToValue;
import HAPK.AST.EXPRESSIONS.ExToVar;
import HAPK.AST.OPERATIONS.OperationBoo;
import HAPK.AST.OPERATIONS.OperationNumber;
import HAPK.AST.TOKENS.BooValue;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.TOKENS.IntegerLiteral;
import HAPK.AST.TOKENS.Operator;

public interface Visitor {
    Object visitBlock(Block b, Object arg) throws Exception;

    Object visitDeclarationList(DeclarationList dl, Object arg) throws Exception;

    Object visitParameterList(ParameterList pl, Object arg) throws Exception;

    Object visitStatement(Statement s, Object arg) throws Exception;

    Object visitIdentifier(Identifier i, Object arg) throws Exception;

    Object visitIntegerLiteral(IntegerLiteral i, Object arg) throws Exception;

    Object visitOperator(Operator o, Object arg) throws Exception;

    Object visitDeMethod(DeMethod deMethod, Object arg) throws Exception;

    Object visitDeInitialization(DeVariable deInitialization, Object arg) throws Exception;

    Object visitExToBoo(ExToBoo exToBoo, Object arg) throws Exception;

    Object visitExToValue(ExToValue exToValue, Object arg) throws Exception;

    Object visitExToVar(ExToVar exToVar, Object arg) throws Exception;

    Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception;

    Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception;

    Object visitProgram(Program program, Object arg) throws Exception;

    Object visitDeArray(DeArray deArray, Object arg) throws Exception;

    Object visitBooValue(BooValue booValue, Object arg) throws Exception;

    Object visitVariableType(VariableType variableType, Object arg) throws Exception;

    Object visitMethodCall(MethodCall methodCall, Object arg) throws Exception;

    Object visitOperationNumbers(OperationNumber operationNumbers, Object arg) throws Exception;

    Object visitOperationBoo(OperationBoo operationBoo, Object arg) throws Exception;

    Object visitSay(Say say, Object arg) throws Exception;
}
