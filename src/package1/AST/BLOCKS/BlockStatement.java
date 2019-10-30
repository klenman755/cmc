package package1.AST.BLOCKS;

import package1.AST.Block;
import package1.AST.Statement;

public abstract class BlockStatement extends Block {
    private Statement statement;

    public BlockStatement(Statement statement) {
        this.statement = statement;
    }
}
