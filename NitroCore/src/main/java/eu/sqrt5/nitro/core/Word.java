package eu.sqrt5.nitro.core;

public class Word<T> {
    public enum Type {
        LITERAL,

        CONSTANT_VALUE, /* 91 */
        CONSTANT_EVENT, /* PlayerJoin */
        CONSTANT_ENTITY, /* ArmorStand */
        CONSTANT_MATERIAL, /* WOOL */

        CODE_BLOCK,
        EXPR_BLOCK,
        DATA_BLOCK
    }

    Type type;
    T value;
    int line_no, line_ch;

    Word(Type type) {
        this.type = type;
    }

    Word(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    Word(Type type, int line_no, int line_ch) {
        this.type = type;
        this.line_no = line_no;
        this.line_ch = line_ch;
    }

    Word(Type type, T value, int line_no, int line_ch) {
        this.type = type;
        this.value = value;
        this.line_no = line_no;
        this.line_ch = line_ch;
    }

    Word(Type type, LexedToken<T> token) {
        this.type = type;
        this.value = token.value;
        this.line_ch = token.line_ch;
        this.line_no = token.line_no;
    }

    Word(LexedToken<T> token) {
        switch (token.type) {
            case LITERAL -> {
                if (Nitro.entities.contains((String) token.value))       this.type = Type.CONSTANT_ENTITY;
                else if (Nitro.events.contains((String) token.value))    this.type = Type.CONSTANT_EVENT;
                else if (Nitro.materials.contains((String) token.value)) this.type = Type.CONSTANT_MATERIAL;
                else                                                     this.type = Type.LITERAL;
            }

            case CONSTANT -> this.type = Type.CONSTANT_VALUE;
            default       -> throw new UnsupportedOperationException(token.type.toString());
        }

        this.value   = token.value;
        this.line_ch = token.line_ch;
        this.line_no = token.line_no;
    }

    /*
    public void dump(int indent) {
        dump(indent, true);
    }

    public void dump(int indent, boolean startIndent) {
        String str;

        if (type == null) {
            System.out.println(startIndent ? "<null>".indent(indent)
                                           : "<null>");
            return;
        }

        switch (type) {
            case LITERAL            -> str = "LITERAL `"          + value + "`";
            case CODE_BLOCK         -> str = "CODE BLOCK:";
            case DATA_BLOCK         -> str = "DATA BLOCK:";
            case EXPR_BLOCK         -> str = "EXPR BLOCK:";
            case CONSTANT_EVENT     -> str = "CONSTANT EVENT "    + value;
            case CONSTANT_VALUE     -> str = "CONSTANT "          + (value.getClass().getSimpleName().equals("String") ? "\"" + value + "\"" : value);
            case CONSTANT_ENTITY    -> str = "CONSTANT ENTITY "   + value;
            case CONSTANT_MATERIAL  -> str = "CONSTANT MATERIAL " + value;
            default                 -> str = "?????";
        }

        System.out.print(startIndent ? str.indent(indent) : str);

        if (type == Type.CODE_BLOCK ||
            type == Type.DATA_BLOCK ||
            type == Type.EXPR_BLOCK) {
            ((Dumpable) value).dump(indent + 1);
        }
    }
    */
}
