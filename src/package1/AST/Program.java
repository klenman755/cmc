package package1.AST;

import java.util.Vector;

public class Program extends AST {
	public Vector<Block> blocks;

	public Program( Vector<Block> blocks )
	{
		this.blocks = blocks;
	}

	public Object visit( Visitor v, Object arg ) throws Exception
	{
		return v.visitProgram( this, arg );
	}
}