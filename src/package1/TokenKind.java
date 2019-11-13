package package1;
/**
 * HAPK
 * @author 
 *
 */


public enum TokenKind {
	
	IDENTIFIER,
	INTEGER_LITERAL,
	BOO_VALUE,
	OPERATOR,
	
	WHILE( "WHILE" ),
	END_WHILE("ENDWHILE"),
	METHOD("METHOD"),
	END_METHOD("ENDMETHOD"),
	IF("IF") ,
	END_IF("ENDIF"),
	THEN("THEN"),

	
	ARRAY("ARRAY"),
	END_ARRAY("ENDARRAY"),

	BOO("BOO"), 
	NUMBER("NUMBER"),
	RETURN("RETURN"),
	WRITE("WRITE"),

	

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
	
	

	
	
	


