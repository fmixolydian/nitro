package eu.sqrt5.nitro.core;

import eu.sqrt5.nitro.core.errors.NitroError;

public class NitroFunction extends Function {
    public final ArgumentList args = new ArgumentList();
    CodeBlock code;

    public Object run(Nitro nitro, Namespace local, int line_no, int line_ch, Object... args) throws NitroError {
        return null;
    };
}
