package package1;

//TODO add logic for different variale types and their checking

public class Parser
{
	private Scanner scan;

	private Token currentTerminal;

	public Parser( Scanner scan ) {
		this.scan = scan;
	}
	
	public void parseProgram() {
		currentTerminal = scan.scan();
		parseBlock();

		if( currentTerminal.kind != TokenKind.EOT ) {
			parseBlock();
		} else {
			System.out.println( "EOT" );
		}
	}

	private void parseBlock() {
		System.out.println("\n-- BLOCK --");
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
				break;
		}
	}
		
	private void parseStatements() {
		System.out.println("\n-- STATEMNETS --");
		switch (currentTerminal.kind) {
			case IDENTIFIER:
				accept(TokenKind.IDENTIFIER);
				switch (currentTerminal.kind) {
					case OPERATOR:
						accept(currentTerminal.kind);

						while (currentTerminal.kind != TokenKind.SEMICOLON) {
							switch (currentTerminal.kind) {
								case INTEGER_LITERAL:
									//TODO accept only declared IDENTIFIERS
								case IDENTIFIER:
									accept(currentTerminal.kind);
									break;
								default:
									System.out.println("Error in statement");
									break;
							}

							if (currentTerminal.kind == TokenKind.SEMICOLON) {
								accept(currentTerminal.kind);
								break;
							}

							if (currentTerminal.kind == TokenKind.OPERATOR) {
								accept(currentTerminal.kind);
							} else {
								System.out.println("Error in statement");
							}
						}
						break;

					//TODO error?? :  IDENTIFIER IDENTIFIER
//					case IDENTIFIER:
//						System.out.println("	ERROR> ? IDENTIFIER : " + currentTerminal.spelling);
//						parseOperations();
//						break;

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
			case INTEGER_LITERAL:
				// TODO RETRUN NUMBER : EXAMPLE 7
				accept(currentTerminal.kind);

				//TODO : EXAMPLE 8 is not allowed, right?
				accept(TokenKind.SEMICOLON);
				break;
			default:
				System.out.println("Error in statement");
				break;
		}
	}
	private void parseEvaluationBlock() {
		System.out.println("\n-- EVALUATION --");
		accept(currentTerminal.kind);
		parseExpression();
		accept(TokenKind.THEN);
		parseStatements();

		// TODO if SCOPE  IF
		//  accept(TokenKind.END_IF);
		//  ELSE
		//  accept(TokenKind.END_WHILE);
		// TODO REMOVE AFTER IMPL
		accept(currentTerminal.kind);
	}

	private void parseExpression() {
		System.out.println("\n-- EXPRESSION --");

		//TODO IF TRUE THEN???

		accept(TokenKind.IDENTIFIER);
		// TODO check if comparioson operator?
		accept(TokenKind.OPERATOR);

		switch (currentTerminal.kind) {
			//TODO check the same type for all BOO == BOO
			case IDENTIFIER:
			case INTEGER_LITERAL:
			case BOO_VALUE:
				accept(currentTerminal.kind);
				break;
		}
	}

//we start from start bracket because identifier was already accepted
	private void parseMethodCallsFromStartBracket() {
		System.out.println("\n-- METHOD CALLS ( --");
		accept(TokenKind.START_BRACKET);
		parseParameterList();
		accept(TokenKind.END_BRACKET);
		accept(TokenKind.SEMICOLON);
	}

	private void parseParameterList() {
		System.out.println("\n-- PARAMETER LIST --");
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
						if( currentTerminal.kind == TokenKind.IDENTIFIER ) {
							// TODO accept only if declared previously.
							accept(TokenKind.IDENTIFIER);
						}
						else if( currentTerminal.kind == TokenKind.INTEGER_LITERAL ) {
							accept(TokenKind.INTEGER_LITERAL);
						}
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
		System.out.println("\n-- DECLARATION LIST --");
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
		if( currentTerminal.kind == expected ) {
			System.out.print(currentTerminal.spelling + " ");
			currentTerminal = scan.scan();
		}
		else {
			System.out.println("Expected token of kind " + expected);
		}
	}
}