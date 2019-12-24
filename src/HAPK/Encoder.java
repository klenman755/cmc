package HAPK;

import TAM.Instruction;
import TAM.Machine;
import HAPK.AST.*;
import HAPK.AST.DECLARATIONS.DeArray;
import HAPK.AST.DECLARATIONS.DeMethod;
import HAPK.AST.DECLARATIONS.DeVariable;
import HAPK.AST.EVALUATION_BLOCKS.IfStatement;
import HAPK.AST.EVALUATION_BLOCKS.WhileStatement;
import HAPK.AST.EXPRESSIONS.ExToBoo;
import HAPK.AST.EXPRESSIONS.ExToValue;
import HAPK.AST.EXPRESSIONS.ExToVar;
import HAPK.AST.OPERATIONS.OperationBoo;
import HAPK.AST.OPERATIONS.OperationNumber;
import HAPK.AST.TOKENS.BooValue;
import HAPK.AST.TOKENS.Identifier;
import HAPK.AST.TOKENS.IntegerLiteral;
import HAPK.AST.TOKENS.Operator;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class Encoder implements Visitor {
    private int nextAdr = Machine.CB;
    private int currentLevel = 0;

    private IdentificationTable idTable = new IdentificationTable();

    private void emit(int op, int n, int r, int d) {
        if (n > 255) {
            System.out.println("Operand too long");
            n = 255;
        }

        Instruction instr = new Instruction();
        instr.op = op;
        instr.n = n;
        instr.r = r;
        instr.d = d;
        System.out.println(op + "  " + n + " " + r + " " + d);

        if (nextAdr >= Machine.PB)
            System.out.println("Program too large");
        else
            System.out.println(instr);
        Machine.code[nextAdr++] = instr;
    }

    private void patch(int adr, int d) {
        Machine.code[adr].d = d;
    }

    private int displayRegister(int currentLevel, int entityLevel) {
        if (entityLevel == 0)
            return Machine.SBr;
        else if (currentLevel - entityLevel <= 6)
            return Machine.LBr + currentLevel - entityLevel;
        else {
            System.out.println("Accessing across to many levels");
            return Machine.L6r;
        }
    }


    public void saveTargetProgram(String fileName) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));

            for (int i = Machine.CB; i < nextAdr; ++i)
                Machine.code[i].write(out);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Trouble writing " + fileName);
        }
    }

    public void encode(Program p) throws Exception {
        System.out.println("\n----- ENCODER -----");
        System.out.println("\n");
        p.visit(this, new Address());

        System.out.println("\n----- ENCODER FINITO -----");
    }

    @Override
    public Object visitProgram(Program p, Object arg) throws Exception {
        currentLevel = 0;

        for (Block block : p.blocks) {
            arg = block.visit(this, arg);

        }
        emit(Machine.HALTop, 0, 0, 0);
        return null;
    }


    @Override
    public Object visitBlock(Block b, Object arg) throws Exception {
        int before = nextAdr;
        if (b.decs != null) {
            emit(Machine.PUSHop, 0, 0, 1);
            arg = b.decs.visit(this, arg);
        } else if (b.stats != null) {
            b.stats.visit(this, null);
        }

        return arg;
    }

    @Override
    public Object visitStatement(Statement s, Object arg) throws Exception {

        if (s.operation != null) {
            s.operation.visit(this, null);
        } else if (s.methodCall != null) {
            s.methodCall.visit(this, null);
        } else if (s.evaluationBlock != null) {
            s.evaluationBlock.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitDeclarationList(DeclarationList dl, Object arg) {
        return null;
    }

    @Override
    public Object visitParameterList(ParameterList pl, Object arg) {

        for (Object i : pl.list) {

            if (i instanceof BooValue) {
                BooValue b = (BooValue) i;
                emit(Machine.LOADLop, 1, 0, b.spelling.equals("TRUE") ? 1 : 0);
            }
            if (i instanceof IntegerLiteral) {
                IntegerLiteral b = (IntegerLiteral) i;
                emit(Machine.LOADLop, 1, 0, Integer.valueOf(b.spelling));
            } else {
                Identifier b = (Identifier) i;
                Address adr;
                try {
                    adr = (Address) b.visit(this, new Boolean(false));
                    int register = displayRegister(currentLevel, adr.level);
                    emit(Machine.LOADop, 1, register, adr.displacement);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Object visitIdentifier(Identifier i, Object arg) throws Exception {
        return i.spelling;
    }

    @Override
    public Object visitIntegerLiteral(IntegerLiteral i, Object arg) {
        return i.spelling;
    }

    @Override
    public Object visitOperator(Operator o, Object arg) {
        return o.spelling;
    }

    @Override
    public Object visitDeMethod(DeMethod deMethod, Object arg) throws Exception {
        deMethod.address = new Address(currentLevel, nextAdr);

        ++currentLevel;

        Address adr = new Address((Address) arg);
        visitDeclarationList(deMethod.list, arg);
        for (Statement s : deMethod.statements) {
            visitStatement(s, arg);
        }
        return null;

    }

    @Override
    public Object visitDeInitialization(DeVariable deInitialization, Object arg) throws Exception {
        deInitialization.address = (Address) arg;
        idTable.enter(deInitialization.name.spelling, deInitialization);
        if (deInitialization.value != null) {
            if (deInitialization.type.spelling.equals("NUMBER")) {
                if (deInitialization.value instanceof Identifier) {
                    Identifier i = (Identifier) deInitialization.value;
                    DeVariable dv = (DeVariable) idTable.retrieve(i.spelling);
                    Address a = dv.address;
                    int register = displayRegister(currentLevel, a.level);
                    emit(Machine.STOREop, 1, register, a.displacement);
                } else {
                    IntegerLiteral il = (IntegerLiteral) deInitialization.value;
                    Integer value = Integer.valueOf(il.spelling);

                    int register = displayRegister(currentLevel, deInitialization.address.level);
                    emit(Machine.LOADLop, 1, 0, value);

                    emit(Machine.STOREop, 1, register, deInitialization.address.displacement);
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.putintDisplacement);
                    emit(Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement);
                }
            } else if (deInitialization.type.spelling.equals("BOO")) {
                if (deInitialization.value instanceof Identifier) {
                    Identifier i = (Identifier) deInitialization.value;
                    DeVariable dv = (DeVariable) idTable.retrieve(i.spelling);
                    Address a = dv.address;
                    int register = displayRegister(currentLevel, a.level);
                    emit(Machine.STOREop, 1, register, a.displacement);
                } else {
                    BooValue il = (BooValue) deInitialization.value;

                    Integer value = il.spelling.equals("TRUE") ? 1 : 0;

                    int register = displayRegister(currentLevel, deInitialization.address.level);
                    emit(Machine.LOADLop, 1, 0, value);
                    emit(Machine.STOREop, 1, register, deInitialization.address.displacement);
                }
            }
        }

        return new Address((Address) arg, 1);
    }

    @Override
    public Object visitExToBoo(ExToBoo exToBoo, Object arg) throws Exception {
        Address adr = (Address) exToBoo.name.visit(this, new Boolean(false));
        int register = displayRegister(currentLevel, adr.level);

        emit(Machine.LOADop, 1, register, adr.displacement);
        emit(Machine.LOADLop, 1, 0, exToBoo.boo.spelling.equals("TRUE") ? 1 : 0);

        return null;
    }

    @Override
    public Object visitExToValue(ExToValue exToValue, Object arg) throws Exception {
        Address adr = (Address) exToValue.name.visit(this, new Boolean(false));
        int register = displayRegister(currentLevel, adr.level);

        emit(Machine.LOADop, 1, register, adr.displacement);
        emit(Machine.LOADLop, 1, 0, Integer.valueOf(exToValue.value.spelling));

        return null;
    }

    @Override
    public Object visitExToVar(ExToVar exToVar, Object arg) throws Exception {
        Address adr1 = (Address) exToVar.name.visit(this, new Boolean(false));
        int register1 = displayRegister(currentLevel, adr1.level);
        Address adr2 = (Address) exToVar.variable.visit(this, new Boolean(false));
        int register2 = displayRegister(currentLevel, adr2.level);

        emit(Machine.LOADop, 1, register1, adr1.displacement);
        emit(Machine.LOADop, 1, register2, adr2.displacement);
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {


        int jump1Adr = nextAdr;
        ifStatement.expression.visit(this, null);
        emit(Machine.JUMPIFop, 0, Machine.CBr, 0);
        for (Statement s : ifStatement.statements) {
            s.visit(this, null);
        }


        emit(Machine.JUMPop, 0, Machine.CBr, 0);

        patch(jump1Adr, nextAdr);


        return null;
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
        int startAdr = nextAdr;

        whileStatement.expression.visit(this, new Boolean(true));

        int jumpAdr = nextAdr;
        emit(Machine.JUMPIFop, 0, Machine.CBr, 0);

        for (Statement s : whileStatement.statements) {
            s.visit(this, null);
        }

        emit(Machine.JUMPop, 0, Machine.CBr, startAdr);
        patch(jumpAdr, nextAdr);

        return null;
    }

    @Override
    public Object visitDeArray(DeArray deArray, Object arg) throws Exception {
        for (Object o : deArray.parameterList.list) {
            if (o instanceof BooValue) {
                BooValue b = (BooValue) o;
                emit(Machine.LOADLop, 1, 0, b.spelling.equals("TRUE") ? 1 : 0);
            }
            if (o instanceof IntegerLiteral) {
                IntegerLiteral b = (IntegerLiteral) o;
                emit(Machine.LOADLop, 1, 0, Integer.valueOf(b.spelling));
            } else {
                Identifier b = (Identifier) o;
                Address adr;
                try {
                    adr = (Address) b.visit(this, new Boolean(false));
                    int register = displayRegister(currentLevel, adr.level);
                    emit(Machine.LOADop, 1, register, adr.displacement);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Address adr = new Address(currentLevel, nextAdr);
        int register = displayRegister(currentLevel, adr.level);
        emit(Machine.CALLop, register, Machine.CB, adr.displacement);
        return null;
    }

    @Override
    public Object visitBooValue(BooValue booValue, Object arg) {
        return booValue.spelling;
    }

    @Override
    public Object visitVariableType(VariableType variableType, Object arg) {
        return variableType.spelling;
    }

    @Override
    public Object visitMethodCall(MethodCall methodCall, Object arg) throws Exception {

        for (Object o : methodCall.list.list) {
            if (o instanceof BooValue) {
                BooValue b = (BooValue) o;
                emit(Machine.LOADLop, 1, 0, b.spelling.equals("TRUE") ? 1 : 0);
            }
            if (o instanceof IntegerLiteral) {
                IntegerLiteral b = (IntegerLiteral) o;
                emit(Machine.LOADLop, 1, 0, Integer.valueOf(b.spelling));
            } else {
                Identifier b = (Identifier) o;
                Address adr;
                try {
                    adr = (Address) b.visit(this, new Boolean(false));
                    int register = displayRegister(currentLevel, adr.level);
                    emit(Machine.LOADop, 1, register, adr.displacement);
                } catch (Exception e) {
					e.printStackTrace();
				}
            }
        }

        Address adr = new Address(currentLevel, nextAdr);
        int register = displayRegister(currentLevel, adr.level);
        emit(Machine.CALLop, register, Machine.CB, adr.displacement);
        return null;
    }

    @Override
    public Object visitOperationNumbers(OperationNumber operationNumbers, Object arg) throws Exception {

        Address adr = (Address) operationNumbers.identifierOne.visit(this, new Boolean(false));
        int register = displayRegister(currentLevel, adr.level);

        emit(Machine.LOADop, 1, register, adr.displacement);
        emit(Machine.LOADLop, 1, 0, (Integer) operationNumbers.values.get(0));
        if (operationNumbers.operators.get(1).spelling.equals("+"))
            emit(Machine.CALLop, 0, Machine.PBr, Machine.addDisplacement);
        else if (operationNumbers.operators.get(1).spelling.equals("-"))
            emit(Machine.CALLop, 0, Machine.PBr, Machine.subDisplacement);
        else if (operationNumbers.operators.get(1).spelling.equals("*"))
            emit(Machine.CALLop, 0, Machine.PBr, Machine.multDisplacement);
        else if (operationNumbers.operators.get(1).spelling.equals("/"))
            emit(Machine.CALLop, 0, Machine.PBr, Machine.divDisplacement);
        emit(Machine.STOREop, 1, register, adr.displacement);
        return null;
    }

    @Override
    public Object visitOperationBoo(OperationBoo operationBoo, Object arg) throws Exception {
        if (operationBoo.operator.spelling.equals("=")) {
            boolean valueNeeded = ((Boolean) arg).booleanValue();

            Integer value = operationBoo.boo.spelling.equals("TRUE") ? 1 : 0;

            if (valueNeeded)
                emit(Machine.LOADLop, 1, 0, value);
        }
        Address adr = (Address) operationBoo.identifier.visit(this, new Boolean(false));
        int register = displayRegister(currentLevel, adr.level);
        emit(Machine.STOREop, 1, register, adr.displacement);
        return null;
    }

    @Override
    public Object visitSay(Say say, Object arg) throws Exception {
        say.text.visit(this, true);

        emit(Machine.CALLop, 0, Machine.PBr, Machine.putintDisplacement);
        emit(Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement);
        return null;
    }
}
