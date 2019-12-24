package HAPK;

import HAPK.AST.Program;

import javax.swing.*;

public class Compiler {
    private static final String EXAMPLES_DIR = "C:\\Users\\monomalo\\Documents\\GitHub\\cmc\\src\\CodeExamples";
    private static final String HARRY_DIR = "/_repo/intellij/cmc/src/CodeExamples/";

    private static final String userDir = System.getProperty("user.home") + HARRY_DIR;

    public static void main(String[] args) throws Exception {
        JFileChooser fc = new JFileChooser(userDir);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String sourceName = fc.getSelectedFile().getAbsolutePath();
            SourceFile in = new SourceFile(sourceName);

            Scanner s = new Scanner(in);
            Parser p = new Parser(s);

            Checker c = new Checker();
            Encoder e = new Encoder();

            Program program = (Program) p.parseProgram();
            c.check(program);
            e.encode(program);

            String targetName;
            if (sourceName.endsWith(".txt"))
                targetName = sourceName.substring(0, sourceName.length() - 4) + ".tam";
            else
                targetName = sourceName + ".tam";

            e.saveTargetProgram(targetName);
        }
    }
}
