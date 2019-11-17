package package1.AST;

public class Block extends AST
{
	public Declaration decs;
	public Statement stats;


	public Block(Declaration decs) {
		this.decs = decs;
	}


	public Block(Statement stats) {
		this.stats = stats;
	}


	public Object visit( Visitor v, Object arg )
	{
		return v.visitBlock( this, arg );
	}
}