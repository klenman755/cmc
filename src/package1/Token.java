package package1;

public class Token
{
//	public byte kind;
	
	public TokenKind kind;
	public String spelling;

	public Token( TokenKind kind, String spelling )
	{
		this.kind = kind;
		this.spelling = spelling;
		
		if( kind == TokenKind.IDENTIFIER )
/*
			for( byte i = 0; i < SPELLINGS.length; ++i )
				if( spelling.equals( SPELLINGS[i] ) ) {
					this.kind = i;
					break;
				}
*/
			for( TokenKind tk: KEYWORDS )
				if( spelling.equals( tk.getSpelling() ) ) {
					this.kind = tk;
					break;
				}
	}
	
	
	public boolean isAssignOperator()
	{
		if( kind == TokenKind.OPERATOR )
			return containsOperator( spelling, ASSIGNOPS );
		else
			return false;
	}
	
	public boolean isAddOperator()
	{
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
	
	private boolean containsOperator( String spelling, String OPS[] )
	{
		for( int i = 0; i < OPS.length; ++i )
			if( spelling.equals( OPS[i] ) )
				return true;
				
		return false;
	}
	
	

	
	private static final TokenKind[] KEYWORDS = { 

			TokenKind.WHHILE,
			TokenKind.END_WHILE,
			TokenKind.METHOD,
			TokenKind.END_METHOD,
			TokenKind.IF ,
			TokenKind.END_IF,
			TokenKind.THEN,
			TokenKind.STRUCTURE,
			TokenKind.END_STRUCTURE,
			TokenKind.BOO, 
			TokenKind.NUMBER,
			TokenKind.RETURN,
			TokenKind.WRITE,
			TokenKind.TRUE,
			TokenKind.FALSE
			

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
}