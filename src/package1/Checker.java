package package1;

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
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;

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
			if (!(temp instanceof DeVariable)) {
				System.err.println("visitExToBoo() => not the right class type");
				return null;
			}
			DeVariable temp2 = (DeVariable) temp;
			if (!temp2.type.spelling.equals("BOO")) {
				System.err.println("visitExToBoo() => not the right variable type");
				return null;
			}
		}

		if (!exToBoo.doubleEquals.spelling.equals("==")) {
			System.err.println("visitExToBoo() => wrong operator");
			return null;
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
			if ((temp instanceof DeVariable)) {
				DeVariable temp2 = (DeVariable) temp;
				if (!temp2.type.spelling.equals("NUMER")) {
					System.out.println("exToValue() => not the right variable type");
				}
			} else {
				System.out.println("exToValue() => not the right class type");
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
		DeVariable temp2 = null;

		Declaration temp3 = idTable.retrieve(exToVar.variable.spelling);
		DeVariable temp4 = null;

		if (temp == null) {
			System.out.println("visitExToVar() => variable does not exist");
		} else {
			if ((temp instanceof DeVariable)) {
				temp2 = (DeVariable) temp;
			} else {
				System.out.println("visitExToVar() => not the right class type");
			}
		}

		if (!exToVar.doubleEquals.spelling.equals("==")) {
			System.out.println("visitExToVar() => wrong operator");
		}

		if (temp3 == null) {
			System.out.println("visitExToVar() => variable does not exist");
		} else {

			temp4 = (DeVariable) temp3;
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
	public Object visitDeInitialization(DeVariable deInitialization, Object arg) {

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

		String id = (String) deInitialization.name.spelling;

		idTable.enter(id, deInitialization);

		return null;
	}

	private boolean checkInnerDeInitialization(DeVariable deInitialization) {
		if (deInitialization.type.spelling.equals("NUMBER")) {
			if (deInitialization.value instanceof IntegerLiteral) {
				return true;
			} else if (deInitialization.value instanceof Identifier) {
				Identifier identifier = (Identifier) deInitialization.value;
				Declaration temp = idTable.retrieve(identifier.spelling);

				if (temp == null) {
					System.out.println("visitDeInitialization() => variable does not exist");
				} else {
					if (temp instanceof DeVariable) {
						DeVariable temp2 = (DeVariable) temp;
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
					if (temp instanceof DeVariable) {
						DeVariable temp2 = (DeVariable) temp;
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

	@Override
	public Object visitDeArray(DeArray deArray, Object arg) {
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
	public Object visitIdentifier(Identifier i, Object arg) {

		if (idTable.retrieve(i.spelling) == null) {
			System.out.println("visitIdentifier() => Identifier does not exist");
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
	public Object visitMethodCall(MethodCall methodCall, Object arg) {
		Declaration method = idTable.retrieve(methodCall.name.spelling);

		// method exists
		if(method != null) {
			// parameters match type and count
			DeMethod methodDecleration = (DeMethod) method;

			int i = 0;
			for(Object argument : methodCall.list.list) {
				String varTypeSpelling = methodDecleration.list.variableTypeList.get(i).spelling;

				if(varTypeSpelling.equals("NUMBER") && argument instanceof IntegerLiteral) {

				}
				else if (varTypeSpelling.equals("BOO") && argument instanceof BooValue) {

				}
				else {
					System.err.println("visitMethodCall() => arguments does not match");
				}

				//TODO check out of bounds
				i++;
			}
			return null;
		} else {
			System.err.println("visitMethodCall() => method not declared");
			return null;
		}
	}

	@Override
	public Object visitOperationNumbers(OperationNumber operationNumbers, Object arg) {
		// right hand side declared?
		Declaration rhs = idTable.retrieve(operationNumbers.identifierOne.spelling);

		if(rhs != null) {
			if(operationNumbers.operators.get(0).spelling.equals("=")) {

				DeVariable varRhs = (DeVariable) rhs;

				if (varRhs.value instanceof IntegerLiteral) {
					// can do operations
					int i = 1;
					// every value has to be integer literal or idendifier

					for (Object obj : operationNumbers.values) {
						if ( isOperator(operationNumbers.operators.get(i))) {

							if (obj instanceof Identifier) {

								// check if this idetifier exists and is the same variable type
								Identifier identifier = (Identifier) obj;
								Declaration lhs = idTable.retrieve(identifier.spelling);
								if (lhs != null) {
									DeVariable varLhs = (DeVariable) lhs;
									if (varLhs.value instanceof IntegerLiteral) {
										// do OPERATIONS
									} else {
										System.err.println("visitOperationNumbers() => LHS varibale type mismatch - " + identifier.spelling);
										return null;
									}
								} else {
									System.err.println("visitOperationNumbers() => LHS varibale does not exist - " + identifier.spelling);
									return null;
								}

								return null;
							}
							else if (obj instanceof IntegerLiteral) {

								// DO CALCULATION?

							} else {
								System.err.println("visitOperationNumbers() => Not correct variable type used on LHS");
								return null;
							}

						} else {
							System.err.println("visitOperationNumbers() => Not correct operator used on LHS");
							return null;
						}

						i++;
					}
				} else {
					System.err.println("visitOperationNumbers() => RHS type not NUMBER, parser error");
					return null;
				}
			} else {
				System.err.println("visitOperationNumbers() => first operator not '=' ");
				return null;
			}
		} else {
			System.err.println("visitOperationNumbers() => RHS not declared");
			return null;
		}

		return null;
	}

	private boolean isOperator(Operator operator) {
		if (operator.spelling.equals("+")) {
			return true;
		}
		else if (operator.spelling.equals("-")) {
			return true;
		}
		else if (operator.spelling.equals("/")) {
			return true;
		}
		else if (operator.spelling.equals("*")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object visitOperationBoo(OperationBoo operationBoo, Object arg) {
		Declaration rhs = idTable.retrieve(operationBoo.identifier.spelling);

		if(rhs != null) {
			if(operationBoo.operator.spelling.equals("=")) {
				DeVariable varRhs = (DeVariable) rhs;

				if (varRhs.value instanceof BooValue) {

					idTable.replace(
							operationBoo.identifier.spelling,
							new DeVariable(
									new VariableType("BOO"),
									new Identifier(operationBoo.identifier.spelling),
									new Operator("="),
									operationBoo.boo
							)
					);

				} else {
					System.err.println("visitOperationNumbers() => booleans can only be reassigned");
				}
			} else {
				System.err.println("visitOperationNumbers() => first operator not '=' ");
				return null;
			}
		} else {
			System.err.println("visitOperationNumbers() => RHS not declared");
			return null;
		}
		return null;
	}
}