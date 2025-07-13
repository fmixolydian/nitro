package eu.sqrt5.nitro.core;

public class SyntaxError extends RuntimeException {
    public String reason;
    public int line_no;
    public int line_ch;

    public SyntaxError(String reason, int line_no, int line_ch) {
        this.reason = reason;
        this.line_no = line_no;
        this.line_ch = line_ch;
    }

    public SyntaxError(String reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        if (line_no != 0) {
            return "Syntax error at " + line_no + ":" + line_ch + " (" + reason + ")";
        } else {
            return "Syntax error (" + reason + ")";
        }
    }
}
