package package1.AST.VALUE_LISTS;

import java.util.Vector;

import package1.AST.ValueList;
import package1.AST.Visitor;
import package1.AST.TOKENS.IntegerLiteral;

public class ValueListIntegerLiteralValue extends ValueList {
	
	 public Vector<IntegerLiteral> numbers = new Vector<IntegerLiteral>();

	public ValueListIntegerLiteralValue(Vector<IntegerLiteral> numbers) {
		this.numbers = numbers;
	}

	@Override
	public Object visit(Visitor v, Object arg) {
		 return v.visitValueListIntegerLiteralValue( this, arg );
	}

	
}
