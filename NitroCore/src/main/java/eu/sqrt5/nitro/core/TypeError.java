package eu.sqrt5.nitro.core;

import eu.sqrt5.nitro.core.errors.NitroError;

public class TypeError extends NitroError {
    public TypeError(String reason, int line_no, int line_ch) {
        super(reason, line_no, line_ch);
    }
}
