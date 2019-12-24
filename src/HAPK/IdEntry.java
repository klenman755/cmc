package HAPK;

import HAPK.AST.Declaration;

public class IdEntry {

    public int level;
    public String name;
    public Declaration declaration;


    public IdEntry(int level, String id, Declaration declaration) {
        this.level = level;
        this.name = id;
        this.declaration = declaration;
    }


    public String toString() {
        return level + "," + name;
    }
}
