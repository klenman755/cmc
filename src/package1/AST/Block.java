package package1.AST;

public class Block extends AST
{
	public Declaration decs;
	public Statement stats;

	public Block(Declaration decs, Statement stats )
	{
		this.decs = decs;
		this.stats = stats;
	}

	public Object visit( Visitor v, Object arg )
	{
		return v.visitBlock( this, arg );
	}
}