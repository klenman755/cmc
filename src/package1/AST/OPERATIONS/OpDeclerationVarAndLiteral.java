package package1.AST.OPERATIONS;

import package1.AST.Operation;
import package1.AST.SUBTYPES.SubOpIdentifier;
import package1.AST.SUBTYPES.SubOpIntegerLiteral;
import package1.AST.TOKENS.Identifier;
import package1.AST.Visitor;

import java.util.Vector;

public class OpDeclerationVarAndLiteral extends Operation {

    Identifier name;
    Vector<SubOpIdentifier> variables;
    Vector<SubOpIntegerLiteral> literals;

    public OpDeclerationVarAndLiteral(Identifier name, Vector<SubOpIdentifier> variables, Vector<SubOpIntegerLiteral> literals) {
        this.name = name;
        this.variables = variables;
        this.literals = literals;
    }

    public Object visit(Visitor v, Object arg) {
        return v.visitOpDeclerationVarAndLiteral( this, arg );
    }
}
