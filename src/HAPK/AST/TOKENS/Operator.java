package HAPK.AST.TOKENS;

import HAPK.AST.Terminal;
import HAPK.AST.Visitor;

public class Operator extends Terminal {

    public Operator(String spelling) {
        this.spelling = spelling;
    }

     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitOperator(this, arg);
    }
}
