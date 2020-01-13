package HAPK;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class Parser {
    private Scanner scan;

    private String indent;

    private Token currentTerminal;

    Parser(Scanner scan) {
        this.scan = scan;
    }

    Object parseProgram() throws Exception {
        System.out.println("\n------------ PARSE ------------");

        currentTerminal = scan.scan();
        Vector<Block> blocks = new Vector<>();


        while (currentTerminal.kind != TokenKind.EOT) {
            blocks.add(parseBlock());
        }

        System.out.println("\n\n\n------------ PARSE FINITO ------------");
        return new Program(blocks);
    }

    private Block parseBlock() throws Exception {
        System.out.println("\n-- BLOCK --");
        switch (currentTerminal.kind) {
            case VARIABLE_TYPE:
            case METHOD:
            case ARRAY:
                return new Block(parseDeclarations());
            case IDENTIFIER:
            case IF:
            case WHILE:
            case SAY:
                return new Block(parseStatement());

            default:
                return null;
        }
    }

    private Statement parseStatement() throws Exception {
        indent = "  ";
        System.out.println("\n" + indent + "-- STATEMNET --");

        switch (currentTerminal.kind) {
            case IDENTIFIER:
                Identifier placeholder = new Identifier(currentTerminal.spelling);
                accept(currentTerminal.kind);
                switch (currentTerminal.kind) {
                    case OPERATOR:
                        return new Statement(parseOpertion(placeholder));
                    case START_BRACKET:
                        return new Statement(parseMethodCallsFromStartBracket(placeholder));
                    default:

                        System.err.println("parseStatements() => error in parsing statements ");
                        return null;
                }

            case IF:
            case WHILE:
                return new Statement(parseEvaluationBlock());
            case SAY:
                accept(TokenKind.SAY);
                Say say = parseSay();
                accept(TokenKind.SEMICOLON);
                return new Statement(say);

            default:
                System.err.println("Error in statement");
                return null;
        }
    }

    private Say parseSay() throws Exception {
        indent = "    ";
        System.out.println("\n" + indent + "-- SAY --");

        StringBuilder stringBuilder = new StringBuilder();

        while (currentTerminal.kind != TokenKind.SEMICOLON && !currentTerminal.spelling.equals("")) {
            stringBuilder.append(currentTerminal.spelling);
            stringBuilder.append(" ");
            accept(currentTerminal.kind);
        }

        if (currentTerminal.spelling.equals("")) {
            System.err.println("parseSay() => expected semicolon");
            accept(currentTerminal.kind);
        } else {
            System.out.println("\n" + indent + "// SAY: " + stringBuilder.toString() + " //");
        }
        return new Say(new Identifier(stringBuilder.toString()));
    }

    private Vector<Statement> parseStatements() throws Exception {
        indent = "  ";
        System.out.println("\n" + indent + "-- STATEMNETS --");

        Vector<Statement> statementVector = new Vector<>();
        do {
            statementVector.add(parseStatement());
        } while (currentTerminal.kind != TokenKind.END_METHOD && currentTerminal.kind != TokenKind.END_IF && currentTerminal.kind != TokenKind.END_WHILE);

        return statementVector;
    }

    private Operation parseOpertion(Identifier placeholder) throws Exception {
        indent = "  ";
        System.out.println("\n" + indent + "-- OPERATION --");
        Vector<Operator> operators = new Vector<>();
        Vector<Object> values = new Vector<>();

        operators.add(new Operator(currentTerminal.spelling));
        //accepting "="
        accept(TokenKind.OPERATOR);

        switch (currentTerminal.kind) {
            case BOO_VALUE:
                BooValue booValue = new BooValue(currentTerminal.spelling);
                accept(currentTerminal.kind);
                accept(TokenKind.SEMICOLON);
                return new OperationBoo(placeholder, operators.get(0), booValue);

            case INTEGER_LITERAL:
                values.add(new IntegerLiteral(currentTerminal.spelling));
                accept(currentTerminal.kind);

                while (currentTerminal.kind == TokenKind.OPERATOR) {
                    operators.add(new Operator(currentTerminal.spelling));
                    accept(currentTerminal.kind);
                    if (currentTerminal.kind == TokenKind.INTEGER_LITERAL) {
                        values.add(new IntegerLiteral(currentTerminal.spelling));

                    } else if (currentTerminal.kind == TokenKind.IDENTIFIER) {
                        values.add(new Identifier(currentTerminal.spelling));

                    } else {
                        System.err.println("parseOpertion() => unexpected expression");
                        return null;
                    }
                    accept(currentTerminal.kind);

                }
                accept(TokenKind.SEMICOLON);
                return new OperationNumber(placeholder, operators, values);
            case IDENTIFIER:
                values.add(new Identifier(currentTerminal.spelling));
                accept(currentTerminal.kind);

                while (currentTerminal.kind == TokenKind.OPERATOR) {
                    operators.add(new Operator(currentTerminal.spelling));
                    accept(currentTerminal.kind);

                    values.add(mapAndReturnObject());
                    accept(currentTerminal.kind);

                }
                accept(TokenKind.SEMICOLON);
                return new OperationNumber(placeholder, operators, values);


            default:
                System.err.println("parseOpertion() => Error in statement");
                return null;
        }
    }

    private EvaluationBlock parseEvaluationBlock() throws Exception {
        indent = "    ";
        System.out.println("\n" + indent + "-- EVALUATION --");
        switch (currentTerminal.kind) {
            case IF:
                accept(currentTerminal.kind);
                Expression expressionIf = parseExpression();
                accept(TokenKind.THEN);
                Vector<Statement> statementsIf = parseStatements();
                accept(TokenKind.END_IF);
                return new IfStatement(expressionIf, statementsIf);
            case WHILE:
                accept(currentTerminal.kind);
                Expression expressionWhile = parseExpression();
                accept(TokenKind.THEN);
                Vector<Statement> statementsWhile = parseStatements();
                accept(TokenKind.END_WHILE);
                return new WhileStatement(expressionWhile, statementsWhile);
            default:
                System.err.println("parseEvaluationBlock() => error parsing evaluation block");
                return null;
        }
    }

    private Expression parseExpression() throws Exception {
        indent = "    ";
        System.out.println("\n" + indent + "-- EXPRESSION --");

        switch (currentTerminal.kind) {
            case IDENTIFIER:
                Identifier leftSideIdentifier = new Identifier(currentTerminal.spelling);
                accept(currentTerminal.kind);
                Operator operator = new Operator(currentTerminal.spelling);
                accept(TokenKind.OPERATOR);
                switch (currentTerminal.kind) {
                    case IDENTIFIER:
                        Identifier rightSideIndentifier = new Identifier(currentTerminal.spelling);
                        accept(currentTerminal.kind);
                        return new ExToVar(leftSideIdentifier, operator, rightSideIndentifier);

                    case BOO_VALUE:
                        BooValue booValue = new BooValue(currentTerminal.spelling);
                        accept(currentTerminal.kind);
                        return new ExToBoo(leftSideIdentifier, operator, booValue);
                    case INTEGER_LITERAL:
                        IntegerLiteral integerLiteralValue = new IntegerLiteral(currentTerminal.spelling);
                        accept(currentTerminal.kind);
                        return new ExToValue(leftSideIdentifier, operator, integerLiteralValue);
                    default:
                        System.err.println("error in parsing expression: wrong value type");
                        return null;
                }
            case BOO_VALUE:
                BooValue booValue = new BooValue(currentTerminal.spelling);
                accept(currentTerminal.kind);
                Operator operatorBooValue = new Operator(currentTerminal.spelling);
                accept(TokenKind.OPERATOR);
                Identifier rightSideIndentifier = new Identifier(currentTerminal.spelling);
                accept(TokenKind.IDENTIFIER);
                return new ExToBoo(rightSideIndentifier, operatorBooValue, booValue);
            case INTEGER_LITERAL:
                IntegerLiteral integerLiteralValue = new IntegerLiteral(currentTerminal.spelling);
                accept(currentTerminal.kind);
                Operator operatorIntegerLiteral = new Operator(currentTerminal.spelling);
                accept(TokenKind.OPERATOR);
                Identifier rightSideIndentifier2 = new Identifier(currentTerminal.spelling);
                accept(TokenKind.IDENTIFIER);
                return new ExToValue(rightSideIndentifier2, operatorIntegerLiteral, integerLiteralValue);

            default:
                System.err.println("error in parsing expression: wrong value type");
                return null;
        }

    }

    //we start from start bracket because identifier was already accepted
    private MethodCall parseMethodCallsFromStartBracket(Identifier placeholder) throws Exception {
        indent = "    ";
        System.out.println("\n" + indent + "-- METHOD CALLS ( --");
        accept(TokenKind.START_BRACKET);
        ParameterList parameters = parseParameterList();
        accept(TokenKind.END_BRACKET);
        accept(TokenKind.SEMICOLON);
        return new MethodCall(placeholder, parameters);
    }

    private ParameterList parseParameterList() throws Exception {
        ParameterList parameters = new ParameterList();
        System.out.println("\n-- PARAMETER LIST --");
        switch (currentTerminal.kind) {
            case END_BRACKET:
                return parameters;
            case IDENTIFIER:
            case INTEGER_LITERAL:
            case BOO_VALUE:
                parameters.list.add(mapAndReturnObject());
                accept(currentTerminal.kind);
                while (currentTerminal.kind != TokenKind.END_BRACKET) {
                    while (currentTerminal.kind != TokenKind.END_ARRAY) {
                        accept(TokenKind.COMMA);
                        parameters.list.add(mapAndReturnObject());
                        accept(currentTerminal.kind);
                    }
                }
                return parameters;
            default:
                System.err.println("Error in statement");
                return null;
        }

    }

    private Declaration parseInitialization() throws Exception {
        indent = "      ";
        System.out.println("\n" + indent + "-- INITIALIZATION --");

        VariableType variableType = new VariableType(currentTerminal.spelling);
        accept(currentTerminal.kind);
        Identifier identifier = new Identifier(currentTerminal.spelling);
        accept(TokenKind.IDENTIFIER);

        switch (currentTerminal.kind) {
            case SEMICOLON:
                accept(currentTerminal.kind);
                return new DeVariable(variableType, identifier, null, null);
            case OPERATOR:
                Operator operator = new Operator(currentTerminal.spelling);
                Object value;
                accept(currentTerminal.kind);

                if (currentTerminal.kind == TokenKind.INTEGER_LITERAL) {
                    value = new IntegerLiteral(currentTerminal.spelling);

                } else if (currentTerminal.kind == TokenKind.IDENTIFIER) {
                    value = new Identifier(currentTerminal.spelling);

                } else if (currentTerminal.kind == TokenKind.BOO_VALUE) {
                    value = new BooValue(currentTerminal.spelling);

                } else if (currentTerminal.kind == TokenKind.INPUT) {
                    value = parseInput(variableType);

                } else {
                    System.err.println("parseInitialization() => unexpected expression");
                    return null;
                }
                accept(currentTerminal.kind);
                accept(TokenKind.SEMICOLON);

                return new DeVariable(variableType, identifier, operator, value);

            default:
                System.err.println("Error in statement");
                return null;
        }
    }

    private Object parseInput(VariableType variableType) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";

        try {
            input = reader.readLine();
        } catch (IOException e) {
            System.err.println("parseInput() => Could not read input");
            return null;
        }

        if (variableType.spelling.equals("BOO")) {
            if (input.equals("TRUE") || input.equals("FALSE")) {
                return new BooValue(input);
            } else {
                System.err.println("parseInput() => input not boolean: " + input);
                return null;
            }
        } else if (variableType.spelling.equals("NUMBER")) {
            try {
                Integer.parseInt(input);
                return new IntegerLiteral(input);
            } catch (NumberFormatException e) {
                System.err.println("parseInput() => input not number: " + input);
                return null;
            }
        } else {
            System.err.println("parseInput() => Error in parseInput - no such type");
            return null;
        }
    }

    private Declaration parseDeclarations() throws Exception {
        indent = "  ";
        System.out.println("\n" + indent + "-- DECLARATION --");
        switch (currentTerminal.kind) {
            case VARIABLE_TYPE:
                return parseInitialization();

            case METHOD:
                accept(currentTerminal.kind);
                Identifier identifier = new Identifier(currentTerminal.spelling);
                accept(TokenKind.IDENTIFIER);
                accept(TokenKind.START_BRACKET);
                DeclarationList declarationList = parseDeclarationList();
                accept(TokenKind.END_BRACKET);
                Vector<Statement> statements = parseStatements();
                accept(TokenKind.END_METHOD);

                return new DeMethod(identifier, declarationList, statements);
            case ARRAY:
                accept(currentTerminal.kind);
                VariableType variableType = new VariableType(currentTerminal.spelling);
                accept(TokenKind.VARIABLE_TYPE);
                Identifier arrayIdentifier = new Identifier(currentTerminal.spelling);
                accept(TokenKind.IDENTIFIER);

                return parseArrayValues(variableType, arrayIdentifier);

            default:
                System.err.println("parseDeclarations() => error parsing declartation");
                return null;

        }
    }

    private Declaration parseArrayValues(VariableType variableType, Identifier arrayIdentifier) throws Exception {
        indent = "      ";
        System.out.println("\n" + indent + "-- ARRAY --");
        ParameterList parameterList = new ParameterList();

        boolean skipFirstComma = true;

        while (currentTerminal.kind != TokenKind.END_ARRAY) {
            if (skipFirstComma) {
                skipFirstComma = false;
            } else {
                accept(TokenKind.COMMA);
            }

            parameterList.list.add(mapAndReturnObject());
            accept(currentTerminal.kind);
        }

        accept(TokenKind.END_ARRAY);
        return new DeArray(variableType, arrayIdentifier, parameterList);
    }

    private Object mapAndReturnObject() throws Exception {
        if (currentTerminal.kind == TokenKind.IDENTIFIER) {
            return new Identifier(currentTerminal.spelling);
        } else if (currentTerminal.kind == TokenKind.INTEGER_LITERAL) {
            return new IntegerLiteral(currentTerminal.spelling);
        } else if (currentTerminal.kind == TokenKind.BOO_VALUE) {
            return new BooValue(currentTerminal.spelling);
        } else return null;
    }

    //when creating method
    private DeclarationList parseDeclarationList() throws Exception {
        indent = "        ";
        System.out.println("\n" + indent + "-- DECLARATION LIST --");

        switch (currentTerminal.kind) {
            case END_BRACKET:
                return new DeclarationList(null, null);
            case VARIABLE_TYPE:
                Vector<VariableType> variableTypeList = new Vector<>();
                Vector<Identifier> identifierList = new Vector<>();
                variableTypeList.add(new VariableType(currentTerminal.spelling));
                accept(currentTerminal.kind);
                identifierList.add(new Identifier(currentTerminal.spelling));
                accept(TokenKind.IDENTIFIER);
                while (currentTerminal.kind == TokenKind.COMMA) {
                    accept(TokenKind.COMMA);
                    if (currentTerminal.kind == TokenKind.VARIABLE_TYPE) {
                        variableTypeList.add(new VariableType(currentTerminal.spelling));
                        accept(currentTerminal.kind);
                        identifierList.add(new Identifier(currentTerminal.spelling));
                        accept(TokenKind.IDENTIFIER);
                    } else {
                        System.err.println("parseDeclarationList() => Error in statement");
                        return null;
                    }

                }
                return new DeclarationList(variableTypeList, identifierList);
            default:
                System.err.println("parseDeclarationList => Error in statement");
                return null;
        }
    }

    private void accept(TokenKind expected) throws Exception {
        if (currentTerminal.kind == expected) {
            System.out.print(indent + currentTerminal.spelling + " ");
            indent = "";
            currentTerminal = scan.scan();
        } else {
            System.out.print(indent + "\n\n\n");
            throw new Exception("Expected token of kind " + expected + ", but got " + currentTerminal.kind + " ( " + currentTerminal.spelling + " ) ");
        }
    }
}
