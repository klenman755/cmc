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
import package1.AST.OPERATIONS.OperationNumber;
import package1.AST.OPERATIONS.OperationBoo;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;
import package1.AST.VALUE_LISTS.ValueListBooValue;
import package1.AST.VALUE_LISTS.ValueListIntegerLiteralValue;

import java.util.Vector;

public class Parser {
	private Scanner scan;

	private Token currentTerminal;

	public Parser(Scanner scan) {
		this.scan = scan;
	}

	public Object parseProgram() {
		currentTerminal = scan.scan();
		Vector<Block> blocks = new Vector<Block>();
		

		while (currentTerminal.kind != TokenKind.EOT) {
			blocks.add(parseBlock());
			
		} 
		System.out.println("FINITO");
		return new Program(blocks);
	}

	private Block parseBlock() {
		System.out.println("\n-- BLOCK --");
		switch (currentTerminal.kind) {
		case VARIABLE_TYPE:
		case METHOD:
		case ARRAY:
			return new Block(parseDeclarations());
		case IDENTIFIER:
		case IF:
		case WHILE:
			return new Block( parseStatement());

		default:
			return null;
		}
	}

	private Statement parseStatement() {
		System.out.println("\n-- STATEMNET --");
		switch (currentTerminal.kind) {
			case IDENTIFIER:
				Identifier placeholder = new Identifier(currentTerminal.spelling);
				accept(currentTerminal.kind);
				switch (currentTerminal.kind) {
					case OPERATOR:
						return new Statement(parseOpertion(placeholder));
					case START_BRACKET:
						return new Statement(parseMethodCallsFromStartBracket(placeholder));
					default:

						System.err.println("parseStatements() => error in parsing statements ");
						return null;
				}

			case IF:
			case WHILE:
				 return new Statement(parseEvaluationBlock());

			default:
				System.err.println("Error in statement");
				return null;
		}
	}

	private Vector<Statement> parseStatements() {
		System.out.println("\n-- STATEMNETS --");
		Vector<Statement> statementVector = new Vector<Statement>();
		do {
				statementVector.add(parseStatement());
		} while ( currentTerminal.kind != TokenKind.END_METHOD && currentTerminal.kind != TokenKind.END_IF &&  currentTerminal.kind != TokenKind.END_WHILE );

		return statementVector;
	}

	private Operation parseOpertion(Identifier placeholder) {
		Vector<Operator> operators = new Vector<Operator>();
		Vector<Object> values = new Vector<Object>();

		operators.add(new Operator(currentTerminal.spelling));
		//accepting "="
		accept(TokenKind.OPERATOR);

		switch (currentTerminal.kind) {
			case BOO_VALUE:
				BooValue booValue = new BooValue(currentTerminal.spelling);
				accept(currentTerminal.kind);
				accept(TokenKind.SEMICOLON);
				return new OperationBoo(placeholder, operators.get(0), booValue);

			case INTEGER_LITERAL:
				values.add(new IntegerLiteral(currentTerminal.spelling)); 
				accept(currentTerminal.kind);

				while(currentTerminal.kind==TokenKind.OPERATOR) {
					operators.add(new Operator(currentTerminal.spelling));
					accept(currentTerminal.kind);
					if(currentTerminal.kind==TokenKind.INTEGER_LITERAL) {
						values.add(new IntegerLiteral(currentTerminal.spelling));
						
					}else if(currentTerminal.kind==TokenKind.IDENTIFIER) {
						values.add(new Identifier(currentTerminal.spelling));
						
					}else {
						System.err.println("parseOpertion() => unexpected expression");
						return null;
					}
					accept(currentTerminal.kind);
					
				}
				accept(TokenKind.SEMICOLON);
				return new OperationNumber(placeholder, operators, values);
			case IDENTIFIER:
				values.add(new Identifier(currentTerminal.spelling));
				accept(currentTerminal.kind);
				
				while(currentTerminal.kind==TokenKind.OPERATOR) {
					operators.add(new Operator(currentTerminal.spelling));
					accept(currentTerminal.kind);
					if(currentTerminal.kind==TokenKind.INTEGER_LITERAL) {
						values.add(new IntegerLiteral(currentTerminal.spelling));
						
					}else if(currentTerminal.kind==TokenKind.IDENTIFIER) {
						values.add(new Identifier(currentTerminal.spelling));
						
					}else {
						System.err.println("parseOpertion() => unexpected expression");
						return null;
					}
					accept(currentTerminal.kind);
					
				}
				accept(TokenKind.SEMICOLON);
				return new OperationNumber(placeholder, operators, values);
				
				
				
			default:
				System.err.println("parseOpertion() => Error in statement");
				return null;
		}
	}

	private EvaluationBlock parseEvaluationBlock() {
		System.out.println("\n-- EVALUATION --");
		switch (currentTerminal.kind) {
			case IF:
				accept(currentTerminal.kind);
				Expression expressionIf = parseExpression();
				accept(TokenKind.THEN);
				Vector<Statement> statementsIf = parseStatements();
				accept(TokenKind.END_IF);
				return new IfStatement(expressionIf, statementsIf);
			case WHILE:
				accept(currentTerminal.kind);
				Expression expressionWhile = parseExpression();
				accept(TokenKind.THEN);
				Vector<Statement> statementsWhile = parseStatements();
				accept(TokenKind.END_WHILE);
				return new WhileStatement(expressionWhile, statementsWhile);
			default:
				System.err.println("parseEvaluationBlock() => error parsing evaluation block");
				return null;
		}
	}

	private Expression parseExpression() {
		System.out.println("\n-- EXPRESSION --");

		switch (currentTerminal.kind) {
		case IDENTIFIER:
			Identifier leftSideIdentifier = new Identifier(currentTerminal.spelling);
			accept(currentTerminal.kind);
			Operator operator = new Operator(currentTerminal.spelling);
			accept(TokenKind.OPERATOR);
			switch (currentTerminal.kind) {
			case IDENTIFIER:
				Identifier rightSideIndentifier = new Identifier(currentTerminal.spelling);
				accept(currentTerminal.kind);
				return new ExToVar(leftSideIdentifier, operator, rightSideIndentifier);

			case BOO_VALUE:
				BooValue booValue = new BooValue(currentTerminal.spelling);
				accept(currentTerminal.kind);
				return new ExToBoo(leftSideIdentifier, operator, booValue);
			case INTEGER_LITERAL:
				IntegerLiteral integerLiteralValue = new IntegerLiteral(currentTerminal.spelling);
				accept(currentTerminal.kind);
				return new ExToValue(leftSideIdentifier, operator, integerLiteralValue);
			default:
				System.err.println("error in parsing expression: wrong value type");
				return null;
			}
		case BOO_VALUE:
			BooValue booValue = new BooValue(currentTerminal.spelling);
			accept(currentTerminal.kind);
			Operator operatorBooValue = new Operator(currentTerminal.spelling);
			accept(TokenKind.OPERATOR);
			Identifier rightSideIndentifier = new Identifier(currentTerminal.spelling);
			accept(TokenKind.IDENTIFIER);
			return new ExToBoo(rightSideIndentifier, operatorBooValue, booValue);
		case INTEGER_LITERAL:
			IntegerLiteral integerLiteralValue = new IntegerLiteral(currentTerminal.spelling);
			accept(currentTerminal.kind);
			Operator operatorIntegerLiteral = new Operator(currentTerminal.spelling);
			accept(TokenKind.OPERATOR);
			Identifier rightSideIndentifier2 = new Identifier(currentTerminal.spelling);
			accept(TokenKind.IDENTIFIER);
			return new ExToValue(rightSideIndentifier2, operatorIntegerLiteral, integerLiteralValue);

		default:
			System.err.println("error in parsing expression: wrong value type");
			return null;
		}

	}

//we start from start bracket because identifier was already accepted
	private MethodCall parseMethodCallsFromStartBracket(Identifier placeholder) {
		System.out.println("\n-- METHOD CALLS ( --");
		accept(TokenKind.START_BRACKET);
		ParameterList parameters = parseParameterList();
		accept(TokenKind.END_BRACKET);
		accept(TokenKind.SEMICOLON);
		return new MethodCall(placeholder, parameters);
	}

	private ParameterList parseParameterList() {
		ParameterList parameters = new ParameterList();
		System.out.println("\n-- PARAMETER LIST --");
		switch (currentTerminal.kind) {
		case END_BRACKET:
			return parameters;
		case IDENTIFIER:
		case INTEGER_LITERAL:
		case BOO_VALUE:
			if(currentTerminal.kind == TokenKind.IDENTIFIER) {
				parameters.list.add(new Identifier(currentTerminal.spelling));
			} else if(currentTerminal.kind == TokenKind.INTEGER_LITERAL) {
				parameters.list.add(new IntegerLiteral(currentTerminal.spelling));
			} else if(currentTerminal.kind == TokenKind.BOO_VALUE) {
				parameters.list.add(new BooValue(currentTerminal.spelling));
			}
			accept(currentTerminal.kind);
			
			while (currentTerminal.kind == TokenKind.COMMA) {
				accept(TokenKind.COMMA);
				switch (currentTerminal.kind) {
				case IDENTIFIER:
				case INTEGER_LITERAL:
				case BOO_VALUE:
					if(currentTerminal.kind == TokenKind.IDENTIFIER) {
						parameters.list.add(new Identifier(currentTerminal.spelling));
					} else if(currentTerminal.kind == TokenKind.INTEGER_LITERAL) {
						parameters.list.add(new IntegerLiteral(currentTerminal.spelling));
					} else if(currentTerminal.kind == TokenKind.BOO_VALUE) {
						parameters.list.add(new BooValue(currentTerminal.spelling));
					}
					accept(currentTerminal.kind);
					break;
				default:
					System.err.println("wrong parameter in parameter list");
					break;
				}
			}
			return parameters;

		default:
			System.err.println("Error in statement");
			return null;
		}

	}

	private ValueList parseValueList() {
		System.out.println("\n--  PARSE VALUE LIST  --");
		switch (currentTerminal.kind) {
		case INTEGER_LITERAL:
			Vector<IntegerLiteral> integerLiteralValues = new Vector<>();
			integerLiteralValues.add(new IntegerLiteral(currentTerminal.spelling));
			accept(currentTerminal.kind);
			while (currentTerminal.kind == TokenKind.COMMA) {
				accept(currentTerminal.kind);
				integerLiteralValues.add(new IntegerLiteral(currentTerminal.spelling));
				accept(TokenKind.INTEGER_LITERAL);
			}
			
			return new ValueListIntegerLiteralValue(integerLiteralValues);
		case BOO_VALUE:
			Vector<BooValue> booValues = new Vector<>();
			booValues.add(new BooValue(currentTerminal.spelling));
			accept(currentTerminal.kind);
			while (currentTerminal.kind == TokenKind.COMMA) {
				accept(currentTerminal.kind);
				booValues.add(new BooValue(currentTerminal.spelling));
				accept(TokenKind.BOO_VALUE);
			}
			
			return new ValueListBooValue(booValues);
		default:
			System.err.println("parseValueList() => error parsing value list");
			return null;

		}

	}

	
	private Declaration parseInitialization() {
		System.out.println("-- INITIALIZATION --");
		VariableType variableType = new VariableType(currentTerminal.spelling);
		accept(currentTerminal.kind);
		Identifier identifier = new Identifier(currentTerminal.spelling);
		accept(TokenKind.IDENTIFIER);

		switch (currentTerminal.kind) {
		case SEMICOLON:
			accept(currentTerminal.kind);
			return new DeInitialization(variableType, identifier, null, null);
		case OPERATOR:
			Operator operator = new Operator(currentTerminal.spelling);
			Object value;
			accept(currentTerminal.kind);

			if (currentTerminal.kind == TokenKind.INTEGER_LITERAL) {
				value = new IntegerLiteral(currentTerminal.spelling);

			} else if (currentTerminal.kind == TokenKind.IDENTIFIER) {
				value = new Identifier(currentTerminal.spelling);

			} else if (currentTerminal.kind == TokenKind.BOO_VALUE) {
				value = new BooValue(currentTerminal.spelling);

			} else {
				System.err.println("parseInitialization() => unexpected expression");
				return null;
			}
			accept(currentTerminal.kind);
			accept(TokenKind.SEMICOLON);
			
			return new DeInitialization(variableType, identifier, operator, value);
			
		default:
			System.err.println("Error in statement");
			return null;
		}

	}

	private Declaration parseDeclarations() {
		System.out.println("\n-- DECLARATION --");
		switch (currentTerminal.kind) {
		case VARIABLE_TYPE:
			return parseInitialization();

		case METHOD:
			accept(currentTerminal.kind);
			Identifier identifier = new Identifier(currentTerminal.spelling);
			accept(TokenKind.IDENTIFIER);
			accept(TokenKind.START_BRACKET);
			DeclarationList declarationList = parseDeclarationList();
			accept(TokenKind.END_BRACKET);
			 Vector<Statement> statements = parseStatements();
			accept(TokenKind.END_METHOD);
			
			return new DeMethod(identifier, declarationList, statements);
		case ARRAY:
			accept(currentTerminal.kind);
			VariableType variableType = new VariableType(currentTerminal.spelling);
			accept(TokenKind.VARIABLE_TYPE);
			Identifier arrayIdentifier = new Identifier(currentTerminal.spelling);
			
			accept(TokenKind.IDENTIFIER);
			switch (currentTerminal.kind) {
			case IDENTIFIER:
				ParameterList parameterList = parseParameterList();
				accept(TokenKind.END_ARRAY);
				return new DeArray(variableType, arrayIdentifier, parameterList);
			
			case INTEGER_LITERAL:
			case BOO_VALUE:
				ValueList valueList = parseValueList();
				accept(TokenKind.END_ARRAY);
				return new DeArray(variableType, arrayIdentifier, valueList);
				
				
			default:

				System.err.println("parseDeclarations() => error parsing an array");
				return null;
			}
			
		
		default:
			System.err.println("parseDeclarations() => error parsing declartation");
			return null;

		}
	}
//when creating method
	private DeclarationList parseDeclarationList() {
		System.out.println("\n-- DECLARATION LIST --");
		
		switch (currentTerminal.kind) {
		case END_BRACKET:
			return  new DeclarationList(null, null);
		case VARIABLE_TYPE:
			Vector<VariableType> variableTypeList = new Vector<VariableType>();
			Vector<Identifier> identifierList = new Vector<Identifier>();
			variableTypeList.add(new VariableType(currentTerminal.spelling));
			accept(currentTerminal.kind);
			identifierList.add(new Identifier(currentTerminal.spelling));
			accept(TokenKind.IDENTIFIER);
			while (currentTerminal.kind == TokenKind.COMMA) {
				accept(TokenKind.COMMA);
				if (currentTerminal.kind == TokenKind.VARIABLE_TYPE) {
					variableTypeList.add(new VariableType(currentTerminal.spelling));
					accept(currentTerminal.kind);
					identifierList.add(new Identifier(currentTerminal.spelling));
					accept(TokenKind.IDENTIFIER);
				} else {
					System.err.println("parseDeclarationList() => Error in statement");
				return null;
				}
				
			}
			return new DeclarationList(variableTypeList, identifierList);
		default:
			System.err.println("parseDeclarationList => Error in statement");
			return null;
		}
	}

	private void accept(TokenKind expected) {
		if (currentTerminal.kind == expected) {
			System.out.print(currentTerminal.spelling + " ");
			currentTerminal = scan.scan();
		} else {
			System.err.println("Expected token of kind " + expected);
		}
	}
}