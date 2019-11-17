package package1.AST.VALUE_LISTS;

import java.util.Vector;

import package1.AST.ValueList;
import package1.AST.Visitor;
import package1.AST.TOKENS.BooValue;

public class ValueListBooValue extends ValueList {
	
	 public Vector<BooValue> booleans = new Vector<BooValue>();

	public ValueListBooValue(Vector<BooValue> booleans) {
		this.booleans = booleans;
	}

	  public Object visit(Visitor v, Object arg) {
	        return v.visitValueListBooValue( this, arg );
	    }
}
