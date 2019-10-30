
  
package package1;

//TODO add logic for different variale types and their checking
import package1.TokenKind.*;


public class Parser
{
	private Scanner scan;
	
	
	private Token currentTerminal;
	
	
	public Parser( Scanner scan )
	{
		this.scan = scan;
		
		currentTerminal = scan.scan();
	}
	
	
	public void parseProgram()
	{
		parseBlock();
		
		if( currentTerminal.kind != TokenKind.EOT )
			System.out.println( "Tokens found after end of program" );
	}
	
	
	private void parseBlock() {
		switch (currentTerminal.kind) {
		case BOO:
		case NUMBER:
		case METHOD:
			parseDeclarations();
			break;
		case IDENTIFIER:
		case IF:
		case WHILE:
			parseStatements();
			break;
		default:
			System.out.println("Error in statement");
			break;
		
		}
	}
	
		
		
	private void parseStatements() {
		switch (currentTerminal.kind) {
		case IDENTIFIER:
			accept(TokenKind.IDENTIFIER);
			switch (currentTerminal.kind) {
			case IDENTIFIER:
				parseOperations();
				break;
			case START_BRACKET:
				parseMethodCallsFromStartBracket();
				break;
			default:
				System.out.println("Error in statement");
				break;

			}
			break;
		case IF:
		case WHILE:
			parseEvaluationBlock();
			break;
		default:
			System.out.println("Error in statement");
			break;
		}
	}
	private void parseEvaluationBlock() {
		accept(TokenKind.IF);
		parseExpression();
		accept(TokenKind.THEN);
		parseStatements();
		accept(TokenKind.END_IF);
		
	}


	private void parseExpression() {
		accept(TokenKind.IDENTIFIER);
		accept(TokenKind.OPERATOR);
	}

//we start from start bracket because identifier was already accepted
	private void parseMethodCallsFromStartBracket() {
		accept(TokenKind.START_BRACKET);
		parseParameterList();
		accept(TokenKind.END_BRACKET);
		accept(TokenKind.SEMICOLON);
		
	}


	private void parseParameterList() {
		switch (currentTerminal.kind) {
		case END_BRACKET:
			break;
		case IDENTIFIER:
			accept(TokenKind.IDENTIFIER);
			while(currentTerminal.kind == TokenKind.COMMA) {
				accept(TokenKind.COMMA);
				accept(TokenKind.IDENTIFIER);
			}
			break;
		default:
			System.out.println("Error in statement");
			break;
		
		
		}
		
	}


	//we start from operator because identifier is already accepted
	private void parseOperations() {
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
			case START_BRACKET:
				parseMethodCallsFromStartBracket();
				break;
			default:
				System.out.println("Error in statement");
				break;
			}
		default:
			System.out.println("Error in statement");
			break;
		}

	}


	private void parseDeclarations() {
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
				accept(TokenKind.INTEGER_LITERAL);
				accept(TokenKind.SEMICOLON);
				break;
			default:
				System.out.println("Error in statement");
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
		default:
			System.out.println("Error in statement");
			break;

		}
	}
	

	
	private void parseDeclarationList() {
		switch (currentTerminal.kind) {
		case END_BRACKET:
			break;
		case BOO:
		case NUMBER:
			accept(currentTerminal.kind);
			accept(TokenKind.IDENTIFIER);
			while(currentTerminal.kind == TokenKind.COMMA) {
				accept(TokenKind.COMMA);
				if(currentTerminal.kind == TokenKind.BOO || currentTerminal.kind==TokenKind.NUMBER) {
					accept(currentTerminal.kind);
				}else {
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


	private void accept( TokenKind expected )
	{
		if( currentTerminal.kind == expected )
			currentTerminal = scan.scan();
		else
			System.out.println( "Expected token of kind " + expected );
	}
}