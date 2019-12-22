package package1.AST.TOKENS;

import package1.AST.Terminal;
import package1.AST.Visitor;

public class Operator extends Terminal {

    public Operator(String spelling) {
        this.spelling = spelling;
    }

     public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitOperator(this, arg);
    }
}