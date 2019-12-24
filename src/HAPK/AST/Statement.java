package HAPK.AST;

public class Statement extends AST {
    public Operation operation;
    public MethodCall methodCall;
    public EvaluationBlock evaluationBlock;
    public Say say;

    public Statement(Operation operation) {
        this.operation = operation;
    }

    public Statement(MethodCall methodCall) {
        this.methodCall = methodCall;
    }

    public Statement(EvaluationBlock evaluationBlock) {
        this.evaluationBlock = evaluationBlock;
    }

    public Statement(Say say) {
        this.say = say;
    }

    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitStatement(this, arg);
    }
}
