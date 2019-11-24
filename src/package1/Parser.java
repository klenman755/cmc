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
import package1.AST.OPERATIONS.OperationNumber;
import package1.AST.OPERATIONS.OperationBoo;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;

import java.util.Vector;

public class Parser {
	private Scanner scan;

	private String indent;

	private Token currentTerminal;

	Parser(Scanner scan) {
		this.scan = scan;
	}

	Object parseProgram() {
		currentTerminal = scan.scan();
		Vector<Block> blocks = new Vector<>();
		

		while (currentTerminal.kind != TokenKind.EOT) {
			blocks.add(parseBlock());
			
		} 
		System.out.println("\n\n\n------------ FINITO ------------");
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
		indent = "  ";
		System.out.println("\n" + indent + "-- STATEMNET --");

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
		indent = "  ";
		System.out.println("\n" + indent + "-- STATEMNETS --");

		Vector<Statement> statementVector = new Vector<>();
		do {
				statementVector.add(parseStatement());
		} while ( currentTerminal.kind != TokenKind.END_METHOD && currentTerminal.kind != TokenKind.END_IF &&  currentTerminal.kind != TokenKind.END_WHILE );

		return statementVector;
	}

	private Operation parseOpertion(Identifier placeholder) {
		indent = "  ";
		System.out.println("\n" + indent + "-- OPERATION --");
		Vector<Operator> operators = new Vector<>();
		Vector<Object> values = new Vector<>();

		operators.add(new Operator(currentTerminal.spelling));
		//accepting "="
		accept(TokenKind.OPERATOR);

		Object xx = currentTerminal.kind;

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

					values.add(mapAndReturnObject());
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
		indent = "    ";
		System.out.println("\n" + indent + "-- EVALUATION --");
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
		indent = "    ";
		System.out.println("\n" + indent + "-- EXPRESSION --");

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
		indent = "    ";
		System.out.println("\n" + indent + "-- METHOD CALLS ( --");
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
			while (currentTerminal.kind != TokenKind.END_BRACKET) {
				while (currentTerminal.kind != TokenKind.END_ARRAY) {
					parameters.list.add(mapAndReturnObject());
					accept(currentTerminal.kind);
					accept(TokenKind.COMMA);
				}
			}
			return parameters;
		default:
			System.err.println("Error in statement");
			return null;
		}

	}

	private Declaration parseInitialization() {
		indent = "      ";
		System.out.println("\n" + indent + "-- INITIALIZATION --");
		VariableType variableType = new VariableType(currentTerminal.spelling);
		accept(currentTerminal.kind);
		Identifier identifier = new Identifier(currentTerminal.spelling);
		accept(TokenKind.IDENTIFIER);

		switch (currentTerminal.kind) {
		case SEMICOLON:
			accept(currentTerminal.kind);
			return new DeVariable(variableType, identifier, null, null);
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
			
			return new DeVariable(variableType, identifier, operator, value);
			
		default:
			System.err.println("Error in statement");
			return null;
		}
	}

	private Declaration parseDeclarations() {
		indent = "  ";
		System.out.println("\n" + indent + "-- DECLARATION --");
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

					return parseArrayValues(variableType, arrayIdentifier);

				default:
			System.err.println("parseDeclarations() => error parsing declartation");
			return null;

		}
	}

	private Declaration parseArrayValues(VariableType variableType, Identifier arrayIdentifier) {
		indent = "      ";
		System.out.println("\n" + indent + "-- ARRAY --");
		ParameterList parameterList = new ParameterList();

		boolean skipFirstComma = true;

		while (currentTerminal.kind != TokenKind.END_ARRAY) {
			if(skipFirstComma) {
				skipFirstComma = false;
			} else {
				accept(TokenKind.COMMA);
			}

			parameterList.list.add(mapAndReturnObject());
			accept(currentTerminal.kind);
		}

		accept(TokenKind.END_ARRAY);
		return new DeArray(variableType, arrayIdentifier, parameterList);
	}

	private Object mapAndReturnObject() {
		if (currentTerminal.kind == TokenKind.IDENTIFIER) {
			return new Identifier(currentTerminal.spelling);
		} else if (currentTerminal.kind == TokenKind.INTEGER_LITERAL) {
			return new IntegerLiteral(currentTerminal.spelling);
		} else if (currentTerminal.kind == TokenKind.BOO_VALUE) {
			return new BooValue(currentTerminal.spelling);
		}
		else return null;
	}

	//when creating method
	private DeclarationList parseDeclarationList() {
		indent = "        ";
		System.out.println("\n" + indent + "-- DECLARATION LIST --");
		
		switch (currentTerminal.kind) {
		case END_BRACKET:
			return  new DeclarationList(null, null);
		case VARIABLE_TYPE:
			Vector<VariableType> variableTypeList = new Vector<>();
			Vector<Identifier> identifierList = new Vector<>();
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
			System.out.print(indent + currentTerminal.spelling + " ");
			indent = "";
			currentTerminal = scan.scan();
		} else {
			System.err.println("Expected token of kind " + expected);
		}
	}
}