package package1.AST.BLOCKS;

import package1.AST.Block;
import package1.AST.Declaration;

public class BlockDeclaration extends Block {
    private Declaration declaration;

    public BlockDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }
}
