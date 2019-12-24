package HAPK.AST;

public class VariableType extends Terminal {

    public VariableType(String spelling) {
        this.spelling = spelling;
    }

    @Override
    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitVariableType(this, arg);
    }

}
