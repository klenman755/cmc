package package1.AST;

import package1.AST.TOKENS.Identifier;

public class Say extends AST {
    public Identifier text;

    public Say(Identifier text) {
        this.text = text;
    }

    public Object visit(Visitor v, Object arg) throws Exception {
        return v.visitSay( this, arg );
    }
}
