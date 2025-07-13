package eu.sqrt5.nitro.core;

public class LexedToken<V> {
    public enum Type {
        LITERAL,    /* foo */
        CONSTANT,   /* "foo" */

        /* punctuation */
        CODE_SEP, /* ; / , */
        DATA_SEP, /* : / = */

        DATA_START,  /* ( */
        DATA_END,    /* ) */
        EXPR_START,  /* [ */
        EXPR_END,    /* ] */
        CODE_START,  /* { */
        CODE_END     /* } */
    }

    public int line_no;
    public int line_ch;
    Type type;
    V value;

    public LexedToken(Type type, V value, int line_no, int line_ch) {
        this.type = type;
        this.value = value;
        this.line_no = line_no;
        this.line_ch = line_ch;
    }

    public LexedToken(Type type, V value) {
        this.type = type;
        this.value = value;
    }

    public LexedToken(Type type) {
        this.type = type;
    }

    public LexedToken(Type type, int line_no, int line_ch) {
        this.type = type;
        this.line_no = line_no;
        this.line_ch = line_ch;
    }

    public String toString() {
        if (value == null) {
            return type.name();
        } else {
            if (value.getClass().getSimpleName().equals("String")) {
                return type.name() + " `" + value + "`";
            } else {
                return type.name() + " " + value;
            }
        }
    }
}
