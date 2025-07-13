package eu.sqrt5.nitro.core.builtins;

import eu.sqrt5.nitro.core.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class Execute extends Function {
    public final ArgumentList args = new ArgumentList(
            new Argument("code", CodeBlock.class)
    );

    public Object run(Nitro nitro, Namespace local, int line_no, int line_ch, Object... args) throws SyntaxError {
        this.args.checkArgs(line_no, line_ch, args);
        ((CodeBlock) args[0]).exec(nitro, local);
        return null;
    }
}
