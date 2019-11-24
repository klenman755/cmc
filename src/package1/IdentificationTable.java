package package1;

import package1.AST.*;
import java.util.Vector;

public class IdentificationTable
{
	private Vector<IdEntry> table = new Vector<IdEntry>();
	private int level = 0;

	public IdentificationTable()
	{
	}
	
	public void enter( String id, Declaration dec)
	{
		IdEntry entry = find( id );
		
		if( entry != null && entry.level == level )
			System.out.println( id + " declared twice" );
		else
			table.add( new IdEntry( level, id, dec) );
	}

	public Declaration retrieve( String id )
	{
		IdEntry entry = find( id );
		
		if( entry != null )
			return entry.declaration;
		else
			return null;
	}
	
	public void openScope()
	{
		++level;
	}
	
	public void closeScope()
	{
		int pos = table.size() - 1;
		while( pos >= 0 && table.get(pos).level == level ) {
			table.remove( pos );
			pos--;
		}
		
		level--;
	}

	private IdEntry find( String id )
	{
		for( int i = table.size() - 1; i >= 0; i-- )
			if( table.get(i).name.equals( id ) )
				return table.get(i);

		return null;
	}

	public void replace( String id, Declaration dec ) {
		IdEntry old = find( id );
		table.remove(old);
		table.add( new IdEntry( level, id, dec) );
	}
}