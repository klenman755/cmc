package package1.AST;

public class Block extends AST
{
	public Declaration decs;
	public Statement stats;
	public MethodCall methodCalls;

	public Block(Declaration decs, Statement stats, MethodCall methodCalls )
	{
		this.decs = decs;
		this.stats = stats;
		this.methodCalls = methodCalls;
	}

	public Object visit( Visitor v, Object arg )
	{
		return v.visitBlock( this, arg );
	}
}