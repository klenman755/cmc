package package1;

import TAM.Instruction;
import TAM.Machine;
import package1.AST.*;
import package1.AST.DECLARATIONS.DeArray;
import package1.AST.DECLARATIONS.DeVariable;
import package1.AST.DECLARATIONS.DeMethod;
import package1.AST.EVALUATION_BLOCKS.IfStatement;
import package1.AST.EVALUATION_BLOCKS.WhileStatement;
import package1.AST.EXPRESSIONS.ExToBoo;
import package1.AST.EXPRESSIONS.ExToValue;
import package1.AST.EXPRESSIONS.ExToVar;
import package1.AST.OPERATIONS.OperationBoo;
import package1.AST.OPERATIONS.OperationNumber;
import package1.AST.TOKENS.*;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class Encoder implements Visitor {
	private int nextAdr = Machine.CB;
	private int currentLevel = 0;

	private IdentificationTable idTable = new IdentificationTable();

	private void emit( int op, int n, int r, int d )
	{
		if( n > 255 ) {
			System.out.println( "Operand too long" );
			n = 255;
		}

		Instruction instr = new Instruction();
		instr.op = op;
		instr.n = n;
		instr.r = r;
		instr.d = d;

		if( nextAdr >= Machine.PB )
			System.out.println( "Program too large" );
		else
			System.out.println(instr);
			Machine.code[nextAdr++] = instr;
	}

	private void patch( int adr, int d )
	{
		Machine.code[adr].d = d;
	}

	private int displayRegister( int currentLevel, int entityLevel )
	{
		if( entityLevel == 0 )
			return Machine.SBr;
		else if( currentLevel - entityLevel <= 6 )
			return Machine.LBr + currentLevel - entityLevel;
		else {
			System.out.println( "Accessing across to many levels" );
			return Machine.L6r;
		}
	}


	public void saveTargetProgram( String fileName )
	{
		try {
			DataOutputStream out = new DataOutputStream( new FileOutputStream( fileName ) );

			for( int i = Machine.CB; i < nextAdr; ++i )
				Machine.code[i].write( out );

			out.close();
		} catch( Exception ex ) {
			ex.printStackTrace();
			System.out.println( "Trouble writing " + fileName );
		}
	}

	public void encode(Program p) throws Exception {
		System.out.println("\n----- ENCODER -----");
		System.out.println("\n");
		p.visit(this, new Address());

		System.out.println("\n----- ENCODER FINITO -----");
	}

	@Override
	public Object visitProgram(Program p, Object arg) throws Exception {
		currentLevel = 0;

		for (Block block : p.blocks) {
			arg = block.visit(this, arg);
			emit( Machine.HALTop, 0, 0, 0 );
		}

		return null;
	}

	@Override
	public Object visitBlock(Block b, Object arg) throws Exception {
		int before = nextAdr;
		emit( Machine.JUMPop, 0, Machine.CB, 0 );
		patch( before, nextAdr );

		if (b.decs != null) {
			arg = b.decs.visit( this, arg );
			emit( Machine.PUSHop, 0, 0, 1 );
		} else if (b.stats != null) {
			b.stats.visit(this, null);
		}

		return arg;
	}

	@Override
	public Object visitStatement(Statement s, Object arg) throws Exception {

		if (s.operation != null) {
			s.operation.visit(this, null);
		} else if (s.methodCall != null) {
			s.methodCall.visit(this, null);
		} else if (s.evaluationBlock != null) {
			s.evaluationBlock.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitVariableType(VariableType variableType, Object arg) {

		return variableType.spelling;
	}

	@Override
	public Object visitExToBoo(ExToBoo exToBoo, Object arg) throws Exception {
		DeVariable var = null;
		try {
			var = (DeVariable) idTable.retrieve(exToBoo.name.spelling);
		} catch (Exception e) {
			throw new Exception("visitExToBoo() => Identifier variable type mismatch");
		}

		if (var == null) {
			throw new Exception("visitExToBoo() => variable does not exist");
		}

		if (!exToBoo.doubleEquals.spelling.equals("==")) {
			throw new Exception("visitExToBoo() => wrong operator");
		}

		if (!var.type.spelling.equals("BOO")) {
			throw new Exception("visitExToBoo() => RHS variable type error");
		}

		exToBoo.boo.visit(this, null);

		return null;
	}

	@Override
	public Object visitExToValue(ExToValue exToValue, Object arg) throws Exception {
		DeVariable var = null;
		try {
			var = (DeVariable) idTable.retrieve(exToValue.name.spelling);
		} catch (Exception e) {
			throw new Exception("visitExToValue() => Identifier variable type mismatch");
		}

		if (var == null) {
			System.err.println("exToValue() => variable does not exist");
		} else {

			if (var.value != null) {
				if (!var.type.spelling.equals("NUMBER")) {
					System.err.println("exToValue() => not the right variable type");
				}
			} else {
				System.err.println("exToValue() => variable is null! not initialized, only declared");
			}

		}

		if (!exToValue.doubleEquals.spelling.equals("==")) {
			System.err.println("exToValue() => wrong operator");
		}

		exToValue.value.visit(this, null);

		return null;
	}

	@Override
	public Object visitExToVar(ExToVar exToVar, Object arg) throws Exception {
		DeVariable rhs = null;
		try {
			rhs = (DeVariable) idTable.retrieve(exToVar.name.spelling);
		} catch (Exception e) {
			throw new Exception("visitExToVar() => Identifier variable type mismatch");
		}

		DeVariable lhs = null;
		try {
			lhs = (DeVariable) idTable.retrieve(exToVar.variable.spelling);
		} catch (Exception e) {
			throw new Exception("visitExToVar() => Identifier variable type mismatch");
		}

		if (rhs == null) {
			throw new Exception("visitExToVar() => variable does not exist");
		}

		if (!exToVar.doubleEquals.spelling.equals("==")) {
			throw new Exception("visitExToVar() => wrong operator");
		}

		if (lhs == null) {
			throw new Exception("visitExToVar() => variable does not exist");

		}

		if (rhs.type != lhs.type) {
			throw new Exception("visitExToVar() => variable type mismatch");
		}

		exToVar.variable.visit(this, null);


		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		ifStatement.expression.visit(this, null);

		for (Statement stmnt : ifStatement.statements)
			stmnt.visit(this, null);

		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		whileStatement.expression.visit(this, null);

		for (Statement stmnt : whileStatement.statements)
			stmnt.visit(this, null);

		return null;
	}

	@Override
	public Object visitDeclarationList(DeclarationList dl, Object arg) {
		return null;
	}

	@Override
	public Object visitParameterList(ParameterList pl, Object arg) {

		for (Object obj : pl.list) {
			if (obj instanceof Identifier) {
				Identifier temp = (Identifier) obj;
				if (idTable.retrieve(temp.spelling) == null) {
					System.err.println("visitParameterList() => variable not declared");
				}
			}
		}
		return null;
	}

	@Override
	public Object visitDeMethod(DeMethod deMethod, Object arg) throws Exception {

		idTable.enter(deMethod.name.spelling, deMethod);
		idTable.openScope();

		deMethod.list.visit(this, null);


		//TODO THIS IS ignored, as satenebts should be read when method is called not declared.
//		for (Statement s : deMethod.statements) {
//			s.visit(this, null);
//		}

		idTable.closeScope();

		return null;
	}

	@Override
	public Object visitDeInitialization(DeVariable deInitialization, Object arg) {
		deInitialization.address = (Address) arg;

		return new Address( (Address) arg, 1 );
	}

	private boolean isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists(DeVariable deInitialization) throws Exception {
		if (deInitialization.type.spelling.equals("NUMBER")) {
			if (deInitialization.value instanceof IntegerLiteral) {
				return true;
			} else if (deInitialization.value instanceof Identifier) {
				DeVariable var = null;
				try {
					var = (DeVariable) idTable.retrieve(((Identifier)deInitialization.value).spelling);
				} catch (Exception e) {
					throw new Exception("isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists() => Identifier variable type mismatch");
				}

				if (var == null) {
					throw new Exception("isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists() => variable does not exist");

				} else {
					if (var.type.spelling.equals("NUMBER")) {
						return true;
					}

				}
				System.err.println("isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists() => variable type mismatch");
				return false;
			}
		} else if (deInitialization.type.spelling.equals("BOO")) {
			if (deInitialization.value instanceof BooValue) {
				return true;
			} else if (deInitialization.value instanceof Identifier) {
				DeVariable var = null;
				try {
					var = (DeVariable) idTable.retrieve(((Identifier)deInitialization.value).spelling);
				} catch (Exception e) {
					throw new Exception("isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists() => Identifier variable type mismatch");
				}

				if (var == null) {
					System.err.println("isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists() => variable does not exist");
				} else {
					if (var.type.spelling.equals("BOO")) {
						return true;
					}
				}
				System.err.println("isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists() => variable type mismatch");
				return false;
			}
		}
		return false;
	}

	@Override
	public Object visitDeArray(DeArray deArray, Object arg) throws Exception {
		if(deArray.type.spelling.equals("NUMBER")) {
			for(Object value : deArray.parameterList.list) {
				if(value instanceof Identifier) {
					Identifier v = (Identifier) value;
					v.visit(this, null);
				}
				else if(value instanceof IntegerLiteral) {
					IntegerLiteral v = (IntegerLiteral) value;
					v.visit(this, null);
				}
				else if(value instanceof BooValue) {
					System.err.println("visitDeArray() => type mismatch");
					return null;
				}
			}
		}
		else if (deArray.type.spelling.equals("BOO")) {
			for(Object value : deArray.parameterList.list) {
				if(value instanceof Identifier) {
					Identifier v = (Identifier) value;
					v.visit(this, null);
				}
				else if(value instanceof IntegerLiteral) {
					System.err.println("visitDeArray() => type mismatch");
					return null;
				}
				else if(value instanceof BooValue) {
					BooValue v = (BooValue) value;
					v.visit(this, null);
				}
			}
		}

		if(idTable.retrieve(deArray.name.spelling) == null) {
			idTable.enter(deArray.name.spelling, deArray);
		} else {
			System.err.println("visitDeArray() => Variable name is already taken");
		}

		return null;
	}

	@Override
	public Object visitIdentifier(Identifier i, Object arg) throws Exception {

		if (idTable.retrieve(i.spelling) == null) {
			throw new Exception("visitIdentifier() => Identifier does not exist");
		}

		return i.spelling;
	}

	@Override
	public Object visitIntegerLiteral(IntegerLiteral i, Object arg) {
		return i.spelling;
	}

	@Override
	public Object visitOperator(Operator o, Object arg) {
		return o.spelling;
	}

	@Override
	public Object visitBooValue(BooValue booValue, Object arg) {
		return booValue.spelling;
	}

	@Override
	public Object visitMethodCall(MethodCall methodCall, Object arg) throws Exception {
		DeMethod method = null;
		try {
			method = (DeMethod) idTable.retrieve(methodCall.name.spelling);
		} catch (Exception e) {
			throw new Exception("visitMethodCall() => expected method call");
		}

		// method exists
		if(method != null) {

			int i = 0;
			idTable.openScope();
			for(Object argument : methodCall.list.list) {
				String varTypeSpelling = method.list.variableTypeList.get(i).spelling;

				if(varTypeSpelling.equals("NUMBER") && argument instanceof IntegerLiteral) {
					// add parameters to table

					DeVariable var = new DeVariable(
							new VariableType("NUMBER"),
							new Identifier(method.list.identifierList.get(i).spelling),
							null,
							 ((IntegerLiteral) argument));

					idTable.enter(method.list.identifierList.get(i).spelling, var);

				}
				else if (varTypeSpelling.equals("BOO") && argument instanceof BooValue) {

					DeVariable var = new DeVariable(
							new VariableType("NUMBER"),
							new Identifier(method.list.identifierList.get(i).spelling),
							null,
							((BooValue) argument));

					idTable.enter(method.list.identifierList.get(i).spelling, var);
				}
				else {
					throw new Exception("visitMethodCall() => arguments does not match");
				}

				//TODO check out of bounds
				i++;
			}
			for (Statement s : method.statements) {
				s.visit(this, null);
			}

			idTable.closeScope();
			return null;
		} else {
			throw new Exception("visitMethodCall() => method not declared");
		}
	}

	@Override
	public Object visitOperationNumbers(OperationNumber operationNumbers, Object arg) throws Exception {
		DeVariable rhs = null;
		try {
			rhs = (DeVariable) idTable.retrieve(operationNumbers.identifierOne.spelling);
		}
		catch (Exception e){
			throw new Exception("visitOperationNumbers() => Identifier variable type mismatch");
		}

		// RHS exists in table
		if(rhs == null) {
			throw new Exception("visitOperationNumbers() => RHS not declared");
		}

		if(!operationNumbers.operators.get(0).spelling.equals("=")) {
			throw new Exception("visitOperationNumbers() => first operator has to be '='");
		}

		if (!rhs.type.spelling.equals("NUMBER")) {
			throw new Exception("variableReAssigning() => RHS BOO, LHS NUMBER :(");
		}

		// is re assigning or operation
		if (operationNumbers.values.size() == 1 && operationNumbers.operators.size() == 1) {
			return variableReAssigning(operationNumbers);
		} else if (operationNumbers.values.size() == operationNumbers.operators.size()) {
			return performOperationsToVariable(operationNumbers);
		} else {
			throw new Exception("visitOperationNumbers() => too many values or operators");
		}
	}

	private Object performOperationsToVariable(OperationNumber operationNumbers) throws Exception {
		// each Value is Number
		// or each identifier is number
		// each operator is operato

		boolean skipFirstOperator = true;
		for (Operator op : operationNumbers.operators) {
			if (skipFirstOperator) {
				skipFirstOperator = false;
			}
			else if(!isOperator(op)) {
				throw new Exception("performOperationsToVariable() => LHS wrong operator");
			}
		}

		for (Object obj : operationNumbers.values) {
			if (obj instanceof IntegerLiteral) {
				// TODO CALCULATION
			}
			else {
				DeVariable lhs = null;
				try {
					lhs = (DeVariable) idTable.retrieve(((Identifier) obj).spelling);
				} catch (Exception e) {
					throw new Exception("performOperationsToVariable() => Identifier variable type mismatch");
				}


				// declared
				if ( lhs == null ) {
					throw new Exception("performOperationsToVariable() => LHS variable not declared");
				}

				if ( lhs.value == null ) {
					throw new Exception("performOperationsToVariable() => LHS variable is not initialized");
				}

				// right type
				if ( !lhs.type.spelling.equals("NUMBER")) {
					throw new Exception("performOperationsToVariable() => LHS variable type mismatch");
				}

				// TODO CALCULATION
			}
		}

		return null;
	}

	private Object variableReAssigning(OperationNumber operationNumbers) throws Exception {
		if (operationNumbers.values.get(0) instanceof Identifier) {
			DeVariable lhsDec = null;
			try {
				lhsDec = (DeVariable) idTable.retrieve(((Identifier)operationNumbers.values.get(0)).spelling);
			} catch (Exception e) {
				throw new Exception("variableReAssigning() => Identifier variable type mismatch");
			}
			if (lhsDec == null) {
				throw new Exception("variableReAssigning() => LHS variable not declared");
			}

			if (lhsDec.value == null) {
				throw new Exception("variableReAssigning() => LHS variable not initialized");
			}

			if (!lhsDec.type.spelling.equals("NUMBER")) {
				throw new Exception("variableReAssigning() => LHS variable not a NUMBER");
			}

			idTable.replaceNumberValue(operationNumbers.identifierOne.spelling, ((IntegerLiteral)lhsDec.value).spelling);


		}
		else if (operationNumbers.values.get(0) instanceof IntegerLiteral) {
			idTable.replaceNumberValue(operationNumbers.identifierOne.spelling, ((IntegerLiteral) operationNumbers.values.get(0)).spelling);
		}
		return null;
	}

	private boolean isOperator(Operator operator) {
		switch (operator.spelling) {
			case "+":
			case "-":
			case "*":
			case "/":
				return true;
			default:
				return false;
		}
	}

	@Override
	public Object visitOperationBoo(OperationBoo operationBoo, Object arg) throws Exception {
		DeVariable rhs = null;
		try {
			rhs = (DeVariable) idTable.retrieve(operationBoo.identifier.spelling);
		} catch (Exception e) {
			throw new Exception("visitOperationBoo() => Identifier variable type mismatch");
		}

		if(rhs == null) {
			throw new Exception("visitOperationNumbers() => RHS not declared");
		}

		if(!operationBoo.operator.spelling.equals("=")) {
			throw new Exception("visitOperationNumbers() => first operator not '=' ");
		}

		if (rhs.value instanceof BooValue) {
			idTable.replaceBooValue(operationBoo.identifier.spelling, operationBoo.boo.spelling);
		} else {
			throw new Exception("visitOperationBoo() => RHS NUMBER, LHS BOO :(");
		}

		return null;
	}

}