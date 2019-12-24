package HAPK.AST;


public abstract class AST {
    public abstract Object visit(Visitor v, Object arg) throws Exception;
}
