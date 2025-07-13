package eu.sqrt5.nitro.core.errors;

public class NitroError extends RuntimeException {

    private int line_no = 0;
    private int line_ch = 0;
    private final String reason;

    public String getMessage() {
        if (line_no == 0 || line_ch == 0) {
            return String.format("%s at %d:%d (%s)",
                    this.getClass(),
                    line_no, line_ch,
                    reason);
        } else {
            return String.format("%s (%s)",
                    this.getClass(),
                    reason);
        }
    }

    public NitroError(String reason, int line_no, int line_ch) {
        this.reason = reason;
        this.line_no = line_no;
        this.line_ch = line_ch;
    }
}
