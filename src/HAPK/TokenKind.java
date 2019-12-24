package HAPK;

/**
 * HAPK
 *
 * @author
 */


public enum TokenKind {

    IDENTIFIER,
    INTEGER_LITERAL,
    BOO_VALUE,
    VARIABLE_TYPE,
    OPERATOR,

    WHILE("WHILE"),
    END_WHILE("ENDWHILE"),
    METHOD("METHOD"),
    END_METHOD("ENDMETHOD"),
    IF("IF"),
    END_IF("ENDIF"),
    THEN("THEN"),

    SAY("SAY"),
    INPUT("INPUT"),

    ARRAY("ARRAY"),
    END_ARRAY("ENDARRAY"),

    RETURN("RETURN"),
    WRITE("WRITE"),

    COMMA(","),
    SEMICOLON(";"),
    START_BRACKET("("),
    END_BRACKET(")"),

    EOT,

    ERROR;

    private String spelling = null;


    private TokenKind() {
    }


    private TokenKind(String spelling) {
        this.spelling = spelling;
    }


    public String getSpelling() {
        return spelling;
    }
}
	
	

	
	
	


