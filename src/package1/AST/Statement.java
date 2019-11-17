package package1.AST;

public class Statement extends AST {
    public Operation operation;
    public MethodCall methodCall;
    public EvaluationBlock evaluationBlock;
    
    public Statement(Operation operation) {
		this.operation = operation;
	}

	public Statement(MethodCall methodCall) {
		this.methodCall = methodCall;
	}

	public Statement(EvaluationBlock evaluationBlock) {
		this.evaluationBlock = evaluationBlock;
	}

	public Object visit(Visitor v, Object arg) {
        return v.visitStatement( this, arg );
    }
}