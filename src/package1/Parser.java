package package1;

import package1.AST.*;
import package1.AST.EVALUATIONBLOCKS.IfStatement;
import package1.AST.EVALUATIONBLOCKS.WhileStatement;
import package1.AST.EXPRESSIONS.ExToBoo;
import package1.AST.EXPRESSIONS.ExToValue;
import package1.AST.EXPRESSIONS.ExToVar;
import package1.AST.OPERATIONS.OpDeclerationBoo;
import package1.AST.TOKENS.BooValue;
import package1.AST.TOKENS.Identifier;
import package1.AST.TOKENS.IntegerLiteral;
import package1.AST.TOKENS.Operator;

import java.util.Vector;

public class Parser {
	private Scanner scan;

	private Token currentTerminal;

	public Parser(Scanner scan) {
		this.scan = scan;
	}

	public Object parseProgram() {
		currentTerminal = scan.scan();
		Block block = parseBlock();

		if (currentTerminal.kind != TokenKind.EOT) {
			parseBlock();
		} else {
			System.out.println("\nEOT");
		}

		return new Program(block);
	}

	private Block parseBlock() {
		System.out.println("\n-- BLOCK --");
		switch (currentTerminal.kind) {
		case BOO:
		case NUMBER:
		case METHOD:
		case ARRAY:
			return new Block(parseDeclarations(), null);
		case IDENTIFIER:
		case IF:
		case WHILE:
			return new Block(null, parseStatement());

		default:
			return new Block(null, null);
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
						return parseOpertion(placeholder);
					case START_BRACKET:
						return parseMethodCallsFromStartBracket(placeholder);
					default:

						System.err.println("parseStatements() => error in parsing statements ");
						return null;
				}

			case IF:
			case WHILE:
				return parseEvaluationBlock();

			default:
				System.out.println("Error in statement");
				return null;
		}
	}

	private Vector<Statement> parseStatements() {
		System.out.println("\n-- STATEMNETS --");
		Vector<Statement> statementVector = new Vector<Statement>();
		Statement statement;
		do {
			statement = parseStatement();
			if (statement != null) {
				statementVector.add(parseStatement());
			}
		} while ( currentTerminal.kind != TokenKind.END_METHOD && currentTerminal.kind != TokenKind.END_IF &&  currentTerminal.kind != TokenKind.END_WHILE && statement != null);

		return statementVector;
	}

	private Operation parseOpertion(Identifier placeholder) {
		Vector<Operator> operators = new Vector<Operator>();

		operators.add(new Operator(currentTerminal.spelling));
		accept(TokenKind.OPERATOR);

		switch (currentTerminal.kind) {
			case BOO_VALUE:
				BooValue booValue = new BooValue(currentTerminal.spelling);
				accept(currentTerminal.kind);
				accept(TokenKind.SEMICOLON);
				return new OpDeclerationBoo(placeholder, operators.get(0), booValue);

			case INTEGER_LITERAL:
				accept(currentTerminal.kind);

				// take first and then WHILE not semicollon AND has operator => loop
				return null;
			case IDENTIFIER:
				accept(currentTerminal.kind);
				return null;

			default:
				System.out.println("Error in statement");
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
				System.out.println("parseEvaluationBlock() => error parsing evaluation block");
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
				System.out.println("error in parsing expression: wrong value type");
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
			System.out.println("error in parsing expression: wrong value type");
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
			parameters.list.add(new Identifier(currentTerminal.spelling));
			accept(currentTerminal.kind);
			while (currentTerminal.kind == TokenKind.COMMA) {
				accept(TokenKind.COMMA);
				switch (currentTerminal.kind) {
				case IDENTIFIER:
				case INTEGER_LITERAL:
				case BOO_VALUE:
					parameters.list.add(new Identifier(currentTerminal.spelling));
					accept(currentTerminal.kind);
					break;
				default:
					System.out.println("wrong parameter in parameter list");
					break;
				}
			}
			return parameters;

		default:
			System.out.println("Error in statement");
			return null;
		}

	}

	private void parseValueList() {

		switch (currentTerminal.kind) {
		case INTEGER_LITERAL:
			accept(currentTerminal.kind);
			while (currentTerminal.kind == TokenKind.COMMA) {
				accept(currentTerminal.kind);
				accept(TokenKind.INTEGER_LITERAL);
			}
			break;
		case BOO_VALUE:
			accept(currentTerminal.kind);
			while (currentTerminal.kind == TokenKind.COMMA) {
				accept(currentTerminal.kind);
				accept(TokenKind.BOO_VALUE);
			}
			break;
		default:
			System.out.println("parseValueList() => error parsing value list");
			break;

		}

	}

	// we start from operator because identifier is already accepted
	private void parseOperations() {
		System.out.println("\n-- OPERATION --");
		accept(TokenKind.OPERATOR);
		switch (currentTerminal.kind) {
		case INTEGER_LITERAL:
			accept(currentTerminal.kind);
			while (currentTerminal.kind == TokenKind.OPERATOR) {
				accept(TokenKind.OPERATOR);
				if (currentTerminal.kind == TokenKind.INTEGER_LITERAL || currentTerminal.kind == TokenKind.IDENTIFIER) {
					accept(currentTerminal.kind);
				}

			}
			accept(TokenKind.SEMICOLON);
			break;
		case IDENTIFIER:
			accept(currentTerminal.kind);
			switch (currentTerminal.kind) {
			case SEMICOLON:
				accept(currentTerminal.kind);
				break;
			case OPERATOR:
				accept(TokenKind.OPERATOR);
				switch (currentTerminal.kind) {
				case INTEGER_LITERAL:
					accept(currentTerminal.kind);
					while (currentTerminal.kind == TokenKind.OPERATOR) {
						accept(TokenKind.OPERATOR);
						if (currentTerminal.kind == TokenKind.INTEGER_LITERAL
								|| currentTerminal.kind == TokenKind.IDENTIFIER) {
							accept(currentTerminal.kind);
						}

					}
					accept(TokenKind.SEMICOLON);
					break;
				default:
					System.out.println("Error in statement");
					break;
				}

			default:
				System.out.println("Error in statement");
				break;
			}
		default:
			System.out.println("Error in statement");
			break;
		}

	}

	private Declaration parseDeclarations() {
		System.out.println("\n-- DECLARATION --");
		switch (currentTerminal.kind) {
		case BOO:
		case NUMBER:
			accept(currentTerminal.kind);
			accept(TokenKind.IDENTIFIER);
			switch (currentTerminal.kind) {
			case SEMICOLON:
				accept(currentTerminal.kind);
				break;
			case OPERATOR:
				accept(currentTerminal.kind);
				if (currentTerminal.kind == TokenKind.IDENTIFIER) {

					accept(TokenKind.IDENTIFIER);
				} else if (currentTerminal.kind == TokenKind.INTEGER_LITERAL) {
					accept(TokenKind.INTEGER_LITERAL);
				}
				accept(TokenKind.SEMICOLON);
				break;
			default:
				System.err.println("Error in statement");
				break;
			}
			break;

		case METHOD:
			accept(currentTerminal.kind);
			accept(TokenKind.IDENTIFIER);
			accept(TokenKind.START_BRACKET);
			parseDeclarationList();
			accept(TokenKind.END_BRACKET);
			parseStatements();
			accept(TokenKind.END_METHOD);
			break;
		case ARRAY:
			accept(currentTerminal.kind);
			if (currentTerminal.kind == TokenKind.BOO || currentTerminal.kind == TokenKind.NUMBER) {
				accept(currentTerminal.kind);
			} else {
				System.err.println("parseDeclarations() => error parsing an array1");
				break;
			}
			accept(TokenKind.IDENTIFIER);
			switch (currentTerminal.kind) {
			case IDENTIFIER:
				parseParameterList();

				break;
			case INTEGER_LITERAL:
			case BOO_VALUE:
				parseValueList();
				break;
			default:

				System.err.println("parseDeclarations() => error parsing an array2");
				break;
			}
			accept(TokenKind.END_ARRAY);
			break;

		default:
			System.err.println("Error in statement");
			break;

		}
		return null;
	}

	private void parseDeclarationList() {
		System.out.println("\n-- DECLARATION LIST --");
		switch (currentTerminal.kind) {
		case END_BRACKET:
			break;
		case BOO:
		case NUMBER:
			accept(currentTerminal.kind);
			accept(TokenKind.IDENTIFIER);
			while (currentTerminal.kind == TokenKind.COMMA) {
				accept(TokenKind.COMMA);
				if (currentTerminal.kind == TokenKind.BOO || currentTerminal.kind == TokenKind.NUMBER) {
					accept(currentTerminal.kind);
				} else {
					System.out.println("Error in statement");
					break;
				}
				accept(TokenKind.IDENTIFIER);
				break;
			}
			break;
		default:
			System.out.println("Error in statement");
			break;
		}
	}

	private void accept(TokenKind expected) {
		if (currentTerminal.kind == expected) {
			System.out.print(currentTerminal.spelling + " ");
			currentTerminal = scan.scan();
		} else {
			System.out.println("Expected token of kind " + expected);
		}
	}
}