package eu.sqrt5.nitro.core;

import eu.sqrt5.nitro.core.errors.NitroError;

import java.util.*;

abstract public class Function {
    public record Argument(String name, Class<?> type) { }

    public static class ArgumentList {
        List<Argument> arguments = new ArrayList<>();

        public void checkArgs(int line_no, int line_ch, Object... args) throws SyntaxError {
            for (int i=0; i<args.length; i++) {
                if (args[i].getClass() != this.arguments.get(i).type) {
                    throw new TypeError(String.format("Expected %s for argument '%s', got %s",
                            this.arguments.get(i).type,
                            this.arguments.get(i).name,
                            args[i].getClass()
                            ), line_no, line_ch
                    );
                }
            }


        }

        public ArgumentList(Argument... entries) {
            arguments.addAll(Arrays.asList(entries));
        }

        public ArgumentList() {

        }
    }

    public final ArgumentList args = new ArgumentList();

    abstract public Object run(Nitro nitro, Namespace local, int line_no, int line_ch, Object... args) throws NitroError;
}
