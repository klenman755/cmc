package HAPK;

public class Token {
	public TokenKind kind;
	public String spelling;

	public Token( TokenKind kind, String spelling )
	{
		this.kind = kind;
		this.spelling = spelling;

		if( kind == TokenKind.IDENTIFIER ) {

			for ( String boo : BOOLEAN ) {
				if (boo.equals(spelling)) {
					this.kind = TokenKind.BOO_VALUE;
					System.out.println("IDENTIFIER cannot be named as BOOLEAN");
					break;
				}
			}

			for ( TokenKind tk : KEYWORDS ) {
				if (spelling.equals(tk.getSpelling())) {
					this.kind = tk;
					break;
				}
			}
		}
	}


	public boolean isAssignOperator() {
		if( kind == TokenKind.OPERATOR )
			return containsOperator( spelling, ASSIGNOPS );
		else
			return false;
	}

	public boolean isAddOperator() {
		if( kind == TokenKind.OPERATOR )
			return containsOperator( spelling, ADDOPS );
		else
			return false;
	}

	public boolean isMulOperator()
	{
		if( kind == TokenKind.OPERATOR )
			return containsOperator( spelling, MULTOPS );
		else
			return false;
	}

	public boolean isCompOperator()
	{
		if( kind == TokenKind.OPERATOR )
			return containsOperator( spelling, COMPOPS );
		else
			return false;
	}


	public boolean isVariableType()
	{
		if( kind == TokenKind.VARIABLE_TYPE )
			return containsOperator( spelling, VARIABLE_TYPE );
		else
			return false;
	}
	public boolean isBoolean()
	{
		if( kind == TokenKind.BOO_VALUE )
			return containsOperator( spelling, BOOLEAN );
		else
			return false;
	}

	private boolean containsOperator( String spelling, String OPS[] )
	{
		for( int i = 0; i < OPS.length; ++i )
			if( spelling.equals( OPS[i] ) )
				return true;

		return false;
	}

	private static final TokenKind[] KEYWORDS = {
			TokenKind.WHILE,
			TokenKind.END_WHILE,
			TokenKind.METHOD,
			TokenKind.END_METHOD,
			TokenKind.IF ,
			TokenKind.END_IF,
			TokenKind.THEN,
			TokenKind.SAY,
			TokenKind.INPUT,
			TokenKind.ARRAY,
			TokenKind.END_ARRAY,
			TokenKind.VARIABLE_TYPE,
			TokenKind.RETURN,
			TokenKind.WRITE,
	};


	private static final String VARIABLE_TYPE[] = {
        "BOO", 	"NUMBER"
	};

	private static final String ASSIGNOPS[] =
	{
		"="
	};


	private static final String ADDOPS[] =
	{
		"+",
		"-"
	};


	private static final String MULTOPS[] =
	{
		"*",
		"/"
	};

	private static final String COMPOPS[] =
	{
		"==",
		"!="
	};

	private static final String BOOLEAN[] =
	{
		"TRUE",
		"FALSE"
	};
}
