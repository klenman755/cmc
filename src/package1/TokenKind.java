package package1;

public enum TokenKind {
	
	IDENTIFIER,
	INTEGER_LITERAL,
	OPERATOR,
	
	WHHILE( "WHILE" ),
	END_WHILE("ENDWHILE"),
	METHOD("METHOD"),
	END_METHOD("ENDMETHOD"),
	IF("IF") ,
	END_IF("ENDIF"),
	THEN("THEN"),
	STRUCTURE("STRUCTURE"),
	END_STRUCTURE("ENDSTRUCTURE"),
	BOO("BOO"), 
	NUMBER("NUMBER"),
	RETURN("RETURN"),
	WRITE("WRITE"),
	TRUE("TRUE"),
	FALSE("FALSE"),
	
	
	COMMA(","),
	SEMICOLON(";"),
	START_BRACKET("("),
	END_BRACKET(")"),
	
	EOT,
	
	ERROR;
	
	
	private String spelling = null;
	
	
	private TokenKind()
	{
	}
	
	
	private TokenKind( String spelling )
	{
		this.spelling = spelling;
	}
	
	
	public String getSpelling()
	{
		return spelling;
	}
}
	
	

	
	
	


