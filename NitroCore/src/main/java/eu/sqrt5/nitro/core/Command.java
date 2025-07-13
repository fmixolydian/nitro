package eu.sqrt5.nitro.core;

import java.util.Arrays;
import java.util.List;

public class Command {
    String name;
    List<Word> args;

    Command(String name, Word... args) {
        this.name = name;
        this.args = Arrays.asList(args);
    }
    Command(String name, List<Word> args) {
        this.name = name;
        this.args = args;
    }

    public Object exec(Namespace local) {

    }

    /*
    public void dump(int indent) {
        System.out.println("COMMAND `".indent(indent).stripTrailing() + name + "`:");

        for (Word word : args) {
            word.dump(indent + 1);
        }
    }
    */
}
