package package1;

import package1.AST.*;

public class IdEntry
{
	// TODO are we using this?

	public int level;
	public String id;
	public Declaration attr;
	
	
	public IdEntry( int level, String id, Declaration attr )
	{
		this.level = level;
		this.id = id;
		this.attr = attr;
	}
	
	
	public String toString()
	{
		return level + "," + id;
	}
}