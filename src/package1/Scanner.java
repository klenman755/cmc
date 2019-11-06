/*
 * 23.08.2019 TokenKind enum introduced
 * 16.08.2016 IScanner gone, minor editing
 * 20.09.2010 IScanner
 * 25.09.2009 New package structure
 * 22.09.2006 Original version (based on Example 4.21 in Watt&Brown)
 */

package package1;
//TODO add support for scanning boo
public class Scanner {
	private SourceFile source;

	private char currentChar;
	private StringBuffer currentSpelling = new StringBuffer();

	public Scanner(SourceFile source) {
		this.source = source;

		currentChar = source.getSource();
	}

	private void takeIt() {
		currentSpelling.append(currentChar);
		currentChar = source.getSource();
	}

	private boolean isLetter(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private void scanSeparator() {
		switch (currentChar) {
		case '_':
			takeIt();
			while (currentChar != SourceFile.EOL && currentChar != SourceFile.EOT) {
				takeIt();
			}

			if (currentChar == SourceFile.EOL) {
				takeIt();
			}

			break;

		case ' ':
		case '\n':
		case '\r':
		case '\t':
			takeIt();
			break;
		}
	}

	private TokenKind scanToken() {
		if (isLetter(currentChar)) {
			takeIt();
			while (isLetter(currentChar) || isDigit(currentChar))
				takeIt();
			return TokenKind.IDENTIFIER;

		} else if (isDigit(currentChar)) {
			takeIt();
			while (isDigit(currentChar))
				takeIt();
			return TokenKind.INTEGER_LITERAL;
		}

		switch (currentChar) {
		case '+':
		case '-':
		case '*':
		case '/':
			takeIt();
			return TokenKind.OPERATOR;

		case '=':
			takeIt();
			if (currentChar == '=')
				takeIt();
			return TokenKind.OPERATOR;

		case '!':
			takeIt();
			if (currentChar == '=') {
				takeIt();
				return TokenKind.OPERATOR;
			}

			else {
				return TokenKind.ERROR;
			}

		case ',':
			takeIt();
			return TokenKind.COMMA;

		case ';':
			takeIt();
			return TokenKind.SEMICOLON;

		case '(':
			takeIt();
			return TokenKind.START_BRACKET;

		case ')':
			takeIt();
			return TokenKind.END_BRACKET;

		case SourceFile.EOT:
			return TokenKind.EOT;

		default:
			takeIt();
			return TokenKind.ERROR;
		}
	}

	public Token scan() {
		while (currentChar == '_' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t' || currentChar == ' ') {
			scanSeparator();
		}

		currentSpelling = new StringBuffer("");
		TokenKind kind = scanToken();

		return new Token(kind, new String(currentSpelling));
	}
}
