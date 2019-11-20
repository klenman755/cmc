package package1;

import package1.AST.*;
import package1.AST.DECLARATIONS.DeArray;
import package1.AST.DECLARATIONS.DeInitialization;
import package1.AST.DECLARATIONS.DeMethod;
import package1.AST.EVALUATION_BLOCKS.IfStatement;
import package1.AST.EVALUATION_BLOCKS.WhileStatement;
import package1.AST.EXPRESSIONS.ExToBoo;
import package1.AST.EXPRESSIONS.ExToValue;
import package1.AST.EXPRESSIONS.ExToVar;
import package1.AST.OPERATIONS.OperationBoo;
import package1.AST.OPERATIONS.OperationNumber;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.VALUE_LISTS.ValueListBooValue;
import package1.AST.VALUE_LISTS.ValueListIntegerLiteralValue;

public class Checker implements Visitor {
	private IdentificationTable idTable = new IdentificationTable();

	public void check(Program p) {
		p.visit(this, null);
	}

	@Override
	public Object visitProgram(Program p, Object arg) {
		idTable.openScope();

		for (Block block : p.blocks)
			block.visit(this, null);

		idTable.closeScope();

		return null;
	}

	@Override
	public Object visitBlock(Block b, Object arg) {
		if (b.decs != null) {
			b.decs.visit(this, null);
		} else if (b.stats != null) {
			b.stats.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitVariableType(VariableType variableType, Object arg) {

		return variableType.spelling;
	}

	@Override
	public Object visitStatement(Statement s, Object arg) {

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
	public Object visitExToBoo(ExToBoo exToBoo, Object arg) {
		Declaration temp = idTable.retrieve(exToBoo.name.spelling);

		if (temp == null) {
			System.out.println("visitExToBoo() => variable does not exist");
		} else {
			if (!(temp instanceof DeInitialization)) {
				System.out.println("visitExToBoo() => not the right class type");
			}
			DeInitialization temp2 = (DeInitialization) temp;
			if (!temp2.type.spelling.equals("BOO")) {
				System.out.println("visitExToBoo() => not the right variable type");
			}
		}

		if (!exToBoo.doubleEquals.spelling.equals("==")) {
			System.out.println("visitExToBoo() => wrong operator");
		}

		exToBoo.boo.visit(this, null);

		return null;
	}

	@Override
	public Object visitExToValue(ExToValue exToValue, Object arg) {
		Declaration temp = idTable.retrieve(exToValue.name.spelling);

		if (temp == null) {
			System.out.println("exToValue() => variable does not exist");
		} else {
			if (!(temp instanceof DeInitialization)) {
				System.out.println("exToValue() => not the right class type");
			}
			DeInitialization temp2 = (DeInitialization) temp;
			if (!temp2.type.spelling.equals("NUMER")) {
				System.out.println("exToValue() => not the right variable type");
			}
		}

		if (!exToValue.doubleEquals.spelling.equals("==")) {
			System.out.println("exToValue() => wrong operator");
		}

		exToValue.value.visit(this, null);

		return null;
	}

	@Override
	public Object visitExToVar(ExToVar exToVar, Object arg) {
		Declaration temp = idTable.retrieve(exToVar.name.spelling);
		DeInitialization temp2 = null;

		Declaration temp3 = idTable.retrieve(exToVar.variable.spelling);
		DeInitialization temp4 = null;

		if (temp == null) {
			System.out.println("visitExToVar() => variable does not exist");
		} else {
			if (!(temp instanceof DeInitialization)) {
				System.out.println("visitExToVar() => not the right class type");
			}
			temp2 = (DeInitialization) temp;
		}

		if (!exToVar.doubleEquals.spelling.equals("==")) {
			System.out.println("visitExToVar() => wrong operator");
		}

		if (temp3 == null) {
			System.out.println("visitExToVar() => variable does not exist");
		} else {

			temp4 = (DeInitialization) temp3;
			if (temp2.type != temp4.type) {
				System.out.println("visitExToVar() => variable type mismatch");
			}
			exToVar.variable.visit(this, null);
		}

		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) {
		ifStatement.expression.visit(this, null);

		for (Statement stmnt : ifStatement.statements)
			stmnt.visit(this, null);

		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) {
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
					System.out.println("visitParameterList() => variable not declared");
				}
			}
		}

		return null;
	}

	@Override
	public Object visitDeMethod(DeMethod deMethod, Object arg) {

		idTable.enter(deMethod.name.spelling, deMethod);
		idTable.openScope();

		deMethod.list.visit(this, null);

		for (Statement s : deMethod.statements) {
			s.visit(this, null);
		}

		idTable.closeScope();

		return null;
	}

	@Override
	public Object visitDeInitialization(DeInitialization deInitialization, Object arg) {

		boolean isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists = checkInnerDeInitialization(
				deInitialization);
		if (isRightHandSideVariableTypeEqualToLeftHandSideVariableTypeAndExists) {

			if (idTable.retrieve(deInitialization.name.spelling) == null) {
				if (!deInitialization.equals.equals("=")) {
					System.out.println("visitDeInitialization() => wrong operator");
				}

			} else {
				System.out.println("visitDeInitialization() => The variable already exists");

			}

		}

		String id = (String) deInitialization.name.visit(this, null);

		idTable.enter(id, deInitialization);

		return null;
	}

	private boolean checkInnerDeInitialization(DeInitialization deInitialization) {
		if (deInitialization.type.spelling.equals("NUMBER")) {
			if (deInitialization.value instanceof IntegerLiteral) {
				return true;
			} else if (deInitialization.value instanceof Identifier) {
				Identifier identifier = (Identifier) deInitialization.value;
				Declaration temp = idTable.retrieve(identifier.spelling);

				if (temp == null) {
					System.out.println("visitDeInitialization() => variable does not exist");
				} else {
					if (temp instanceof DeInitialization) {
						DeInitialization temp2 = (DeInitialization) temp;
						if (temp2.type.spelling.equals("NUMBER")) {
							return true;
						}
					}
				}
				System.out.println("visitDeInitialization() => variable type mismatch");
				return false;
			}
		} else if (deInitialization.type.spelling.equals("BOO")) {
			if (deInitialization.value instanceof BooValue) {
				return true;
			} else if (deInitialization.value instanceof Identifier) {
				Identifier identifier = (Identifier) deInitialization.value;
				Declaration temp = idTable.retrieve(identifier.spelling);

				if (temp == null) {
					System.out.println("visitDeInitialization() => variable does not exist");
				} else {
					if (temp instanceof DeInitialization) {
						DeInitialization temp2 = (DeInitialization) temp;
						if (temp2.type.spelling.equals("BOO")) {
							return true;
						}
					}
				}
				System.out.println("visitDeInitialization() => variable type mismatch");
				return false;
			}
		}
		return false;
	}
//**********************************************************************************************//
	@Override
	public Object visitDeArray(DeArray deArray, Object arg) {
		deArray.type.visit(this, null);

		String id = (String) deArray.name.visit(this, null);
		deArray.parameterList.visit(this, null);

		deArray.valueList.visit(this, null);

		idTable.enter(id, deArray);

		return null;
	}

	@Override
	public Object visitDeVariable(DeVariable deVariable, Object arg) {
		deVariable.type.visit(this, null);
		String id = (String) deVariable.name.visit(this, null);

		idTable.enter(id, deVariable);

		return null;
	}

	//
	//
	//

	@Override
	public Object visitIdentifier(Identifier i, Object arg) {
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
	public Object visitBOO(BOO boo, Object arg) {
		return boo.spelling;
	}

	@Override
	public Object visitNUMBER(NUMBER number, Object arg) {
		return number.spelling;
	}

	@Override
	public Object visitMethodCall(MethodCall methodCall, Object arg) {
		methodCall.name.visit(this, null);
		methodCall.list.visit(this, null);
		return null;
	}

	@Override
	public Object visitValueListBooValue(ValueListBooValue valueListBooValue, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitValueListIntegerLiteralValue(ValueListIntegerLiteralValue valueListIntegerLiteralValue,
			Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitDeclaration(Declaration declaration, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitOperationNumbers(OperationNumber operationNumbers, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitOperationBoo(OperationBoo operationBoo, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

}