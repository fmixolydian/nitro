package eu.sqrt5.nitro.core;

import eu.sqrt5.nitro.core.builtins.Execute;

import java.util.Set;

public class Nitro {
    private CodeBlock code;
    private final Namespace globals = new Namespace();

    public static final Set<String> entities = Set.of();
    public static final Set<String> events = Set.of();
    public static final Set<String> materials = Set.of();

    public void load(CodeBlock code) {
        this.code = code;
    }

    public void run() {
        this.code.exec(this, globals);
    }

    public void load(String     code) throws SyntaxError {load(new CodeBlock(code)); }
    public void load(LexedBlock code) throws SyntaxError {load(new CodeBlock(code)); }
    public void run(CodeBlock   code)                    {load(code); run(); }
    public void run(LexedBlock  code) throws SyntaxError {load(code); run(); }
    public void run(String      code) throws SyntaxError {load(code); run(); }

    public Nitro(CodeBlock code) {
        this();
        load(code);
    }

    public Nitro() {
        // load default commands (if, execute, foreach, ...)
        globals.set("execute", new Execute());
    }
}
