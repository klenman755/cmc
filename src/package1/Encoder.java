package package1;

import TAM.Instruction;
import TAM.Machine;
import package1.AST.*;
import package1.AST.DECLARATIONS.DeArray;
import package1.AST.DECLARATIONS.DeVariable;
import package1.AST.DECLARATIONS.DeMethod;
import package1.AST.EVALUATION_BLOCKS.IfStatement;
import package1.AST.EVALUATION_BLOCKS.WhileStatement;
import package1.AST.EXPRESSIONS.ExToBoo;
import package1.AST.EXPRESSIONS.ExToValue;
import package1.AST.EXPRESSIONS.ExToVar;
import package1.AST.OPERATIONS.OperationBoo;
import package1.AST.OPERATIONS.OperationNumber;
import package1.AST.TOKENS.*;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class Encoder implements Visitor {
	private int nextAdr = Machine.CB;
	private int currentLevel = 0;

	private void emit( int op, int n, int r, int d )
	{
		if( n > 255 ) {
			System.out.println( "Operand too long" );
			n = 255;
		}

		Instruction instr = new Instruction();
		instr.op = op;
		instr.n = n;
		instr.r = r;
		instr.d = d;

		if( nextAdr >= Machine.PB )
			System.out.println( "Program too large" );
		else
			System.out.println(instr);
			Machine.code[nextAdr++] = instr;
	}

	private void patch( int adr, int d )
	{
		Machine.code[adr].d = d;
	}

	private int displayRegister( int currentLevel, int entityLevel )
	{
		if( entityLevel == 0 )
			return Machine.SBr;
		else if( currentLevel - entityLevel <= 6 )
			return Machine.LBr + currentLevel - entityLevel;
		else {
			System.out.println( "Accessing across to many levels" );
			return Machine.L6r;
		}
	}


	public void saveTargetProgram( String fileName )
	{
		try {
			DataOutputStream out = new DataOutputStream( new FileOutputStream( fileName ) );

			for( int i = Machine.CB; i < nextAdr; ++i )
				Machine.code[i].write( out );

			out.close();
		} catch( Exception ex ) {
			ex.printStackTrace();
			System.out.println( "Trouble writing " + fileName );
		}
	}

	public void encode(Program p) throws Exception {
		System.out.println("\n----- ENCODER -----");
		System.out.println("\n");

		p.visit(this, new Address());

		emit( Machine.HALTop, 0, 0, 0 );

		System.out.println("\n----- ENCODER FINITO -----");
	}

	@Override
	public Object visitProgram(Program p, Object arg) throws Exception {
		currentLevel = 0;

		for (Block block : p.blocks) {
			arg = block.visit(this, arg);
		}

		return null;
	}

	@Override
	public Object visitBlock(Block b, Object arg) throws Exception {
		int before = nextAdr;
		emit( Machine.JUMPop, 0, Machine.CB, 0 );

		patch( before, nextAdr );

		if (b.decs != null) {
			arg = b.decs.visit( this, arg );
			emit( Machine.PUSHop, 0, 0, 1 );
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
		} else if (s.say != null) {
			s.say.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitVariableType(VariableType variableType, Object arg) throws Exception {
		return variableType.spelling;
	}

	@Override
	public Object visitExToBoo(ExToBoo exToBoo, Object arg) throws Exception {
		boolean valueNeeded = ((Boolean) arg);

		int booVal = (int) exToBoo.boo.visit( this, arg );

		if( valueNeeded && booVal == 1 ) {
			emit( Machine.CALLop, 0, Machine.PBr, Machine.negDisplacement );
		}

		return null;
	}

	@Override
	public Object visitExToValue(ExToValue exToValue, Object arg) throws Exception {
		boolean valueNeeded = ((Boolean) arg);

		int booVal = (int) exToValue.value.visit( this, arg );

		if( valueNeeded && booVal == 1 ) {
			emit( Machine.CALLop, 0, Machine.PBr, Machine.negDisplacement );
		}

		return null;
	}

	@Override
	public Object visitExToVar(ExToVar exToVar, Object arg) throws Exception {
		boolean valueNeeded = ((Boolean) arg);

		int booVal = (int) exToVar.variable.visit( this, arg );

		if( valueNeeded && booVal == 1 ) {
			emit( Machine.CALLop, 0, Machine.PBr, Machine.negDisplacement );
		}

		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		ifStatement.expression.visit( this,  true );

		int jump1Adr = nextAdr;
		emit( Machine.JUMPIFop, 0, Machine.CBr, 0 );

		for(Statement stmt : ifStatement.statements) {
			stmt.visit( this,  null );

			int jump2Adr = nextAdr;
			emit( Machine.JUMPop, 0, Machine.CBr, 0 );
			patch( jump1Adr, nextAdr );

		}
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		whileStatement.expression.visit( this,  true );

		int jump1Adr = nextAdr;
		emit( Machine.JUMPIFop, 0, Machine.CBr, 0 );

		for(Statement stmt : whileStatement.statements) {
			stmt.visit( this,  null );

			int jump2Adr = nextAdr;
			emit( Machine.JUMPop, 0, Machine.CBr, 0 );
			patch( jump1Adr, nextAdr );

		}
		return null;
	}

	@Override
	public Object visitDeclarationList(DeclarationList dl, Object arg) {
		return null;
	}

	@Override
	public Object visitParameterList(ParameterList pl, Object arg) throws Exception {
		return null;
	}

	@Override
	public Object visitDeMethod(DeMethod deMethod, Object arg) throws Exception {
		deMethod.address = (Address) arg;

		return new Address( (Address) arg, 1 );
	}

	@Override
	public Object visitDeInitialization(DeVariable deInitialization, Object arg) throws Exception {
		deInitialization.address = (Address) arg;

		return new Address( (Address) arg, 1 );
	}

	@Override
	public Object visitDeArray(DeArray deArray, Object arg) throws Exception {
		deArray.address = (Address) arg;

		return new Address( (Address) arg, 1 );
	}

	@Override
	public Object visitIdentifier(Identifier i, Object arg) throws Exception {
		return null;
	}

	@Override
	public Object visitIntegerLiteral(IntegerLiteral i, Object arg) throws Exception {
		return i.spelling;
	}

	@Override
	public Object visitOperator(Operator o, Object arg) throws Exception {
		return o.spelling;
	}

	@Override
	public Object visitBooValue(BooValue booValue, Object arg) throws Exception {
		if(booValue.spelling.equals("TRUE")) {
			return 1;
		}
		else if(booValue.spelling.equals("FALSE")) {
			return 0;
		} else {
			return null;
		}
	}

	@Override
	public Object visitMethodCall(MethodCall methodCall, Object arg) throws Exception {
		return null;
	}

	@Override
	public Object visitOperationNumbers(OperationNumber operationNumbers, Object arg) throws Exception {
		return null;
	}

	@Override
	public Object visitOperationBoo(OperationBoo operationBoo, Object arg) throws Exception {
		boolean valueNeeded = ((Boolean) arg);

		operationBoo.identifier.visit(this, arg);

		int booValue = (int) operationBoo.boo.visit(this, arg);

		if (valueNeeded) {
			emit(Machine.LOADLop, 1, 0, booValue);
		}
		return null;
	}

	@Override
	public Object visitSay(Say say, Object arg) throws Exception {
		say.text.visit( this,  true );

		emit( Machine.CALLop, 0, Machine.PBr, Machine.putintDisplacement );
		emit( Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement );

		return null;
	}
}
