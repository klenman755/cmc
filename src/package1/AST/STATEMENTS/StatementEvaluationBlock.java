package package1.AST.STATEMENTS;

import package1.AST.EvaluationBlock;
import package1.AST.Statement;

public class StatementEvaluationBlock extends Statement {
    private EvaluationBlock evaluationBlock;

    public StatementEvaluationBlock(EvaluationBlock evaluationBlock) {
        this.evaluationBlock = evaluationBlock;
    }
}
