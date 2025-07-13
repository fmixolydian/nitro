package eu.sqrt5.nitro.core;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.stream.Collectors;

public class LexedBlock {
    private enum LexMode {
        NORMAL,
        STRING_SINGLE,
        STRING_DOUBLE,
        COMMENT
    };

    List<LexedToken> tokens = new ArrayList<>();

    public static LexedToken next_guarded(Iterator<LexedToken> iterator, LexedToken.Type type, LexedToken.Type ignore) throws SyntaxError {
        if (!iterator.hasNext()) {
            throw new SyntaxError("Expected token " + type + ", got end-of-statement");
        }

        LexedToken token;

        if (ignore == null) {
            token = iterator.next();
        } else {
            try {
                while ((token = iterator.next()).type == ignore) ;
            } catch (NoSuchElementException e) {
                return null;
            }
        }

        if (token.type == type) {
            return token;
        } else {
            throw new SyntaxError("Expected token " + type + ", got " + token.type, token.line_no, token.line_ch);
        }
    }

    public static LexedToken next_guarded(Iterator<LexedToken> iterator, LexedToken.Type type) throws SyntaxError {
        return next_guarded(iterator, type, null);
    }

    public static LexedBlock capture_until(Iterator<LexedToken> iter, LexedToken.Type from, LexedToken.Type until) throws SyntaxError {
        LexedBlock sub = new LexedBlock();
        LexedToken token;

        int nesting = 1;
        while (iter.hasNext()) {
            token = iter.next();
            switch (token.type) {
                case CODE_START:
                case EXPR_START:
                case DATA_START:
                    nesting++;
                    break;

                case CODE_END:
                case EXPR_END:
                case DATA_END:
                    nesting--;
                    break;
            }

            if (nesting == 0) {
                if (token.type == until) {
                    break;
                } else {
                    throw new SyntaxError("Expected " + until + " to end " + from, token.line_no, token.line_ch);
                }
            }

            sub.tokens.add(token);
        }

        if (nesting > 0) {
            throw new SyntaxError("Expected " + until + " to end " + from);
        }

        sub.tokens.add(new LexedToken(LexedToken.Type.CODE_SEP));
        return sub;
    }

    public LexedBlock (String program) {
        // lexer step
        CharacterIterator iterator = new StringCharacterIterator(program);
        StringBuilder buffer = new StringBuilder();
        LexMode mode = LexMode.NORMAL;

        int line_no = 1, line_ch = 0;

        for (char c = iterator.current(); c != CharacterIterator.DONE; c = iterator.next()) {
            if (c == '\n') {
                line_no++;
                line_ch = 0;
            }

            line_ch++;

            if (mode == LexMode.NORMAL) {
                if ("\t\r\n #,;:()[]{}\"'".indexOf(c) > -1) {
                    switch (c) {
                        case '\t':
                        case '\r':
                        case '\n':
                            // whitespace (ignored)
                            break;

                        case '"':
                            // string (double-quoted) start
                            mode = LexMode.STRING_DOUBLE;
                            break;
                        case '\'':
                            // string (single-quoted) start
                            mode = LexMode.STRING_SINGLE;
                            break;
                        case '#':
                            // comment start
                            mode = LexMode.COMMENT;
                            break;
                    }

                    // main tokens
                    if (!buffer.isEmpty() || (c != ' ')) {
                        // System.out.println("lexed token `" + buffer + "` delimiter is `" + c + "`");
                        String s = buffer.toString();

                        if (s.matches("-?[0-9]+") ||
                            s.matches("0x[0-9a-fA-F]+") ||
                            s.matches("0b[01]+]")) {
                            // number constant
                            tokens.add(new LexedToken<Integer>(
                                    LexedToken.Type.CONSTANT,
                                    Integer.parseInt(s),
                                    line_no,
                                    line_ch
                            ));
                        } else if (s.toLowerCase().matches("(false|true)")) {
                            // boolean
                            tokens.add(new LexedToken<Boolean>(
                                    LexedToken.Type.CONSTANT,
                                    Boolean.parseBoolean(s.toLowerCase()),
                                    line_no,
                                    line_ch
                            ));
                        } else {
                            // other shit (literals)
                            if (!buffer.isEmpty()) {
                                tokens.add(new LexedToken<String>(
                                        LexedToken.Type.LITERAL, s,
                                        line_no, line_ch
                                ));
                            }
                        }
                    }

                    // punctuation
                    switch (c) {
                        case ';':
                        case ',': tokens.add(new LexedToken(LexedToken.Type.CODE_SEP,   line_no, line_ch));   break;
                        case ':': tokens.add(new LexedToken(LexedToken.Type.DATA_SEP,   line_no, line_ch));   break;

                        case '(': tokens.add(new LexedToken(LexedToken.Type.DATA_START, line_no, line_ch)); break;
                        case ')': tokens.add(new LexedToken(LexedToken.Type.DATA_END,   line_no, line_ch));   break;
                        case '[': tokens.add(new LexedToken(LexedToken.Type.EXPR_START, line_no, line_ch)); break;
                        case ']': tokens.add(new LexedToken(LexedToken.Type.EXPR_END,   line_no, line_ch));   break;
                        case '{': tokens.add(new LexedToken(LexedToken.Type.CODE_START, line_no, line_ch)); break;
                        case '}': tokens.add(new LexedToken(LexedToken.Type.CODE_END,   line_no, line_ch));   break;
                    }

                    buffer = new StringBuilder();
                } else {
                    buffer.append(c);
                }
            } else {
                switch (mode) {
                    case STRING_DOUBLE:
                        if (c == '"') {
                            // string (double-quoted) end
                            // System.out.println("lexed str \"" + buffer + "\"");
                            mode = LexMode.NORMAL;

                            tokens.add(new LexedToken<String>(
                                    LexedToken.Type.CONSTANT,
                                    buffer.toString()
                            ));

                            buffer = new StringBuilder();
                        } else {
                            buffer.append(c);
                        }
                        break;

                    case STRING_SINGLE:
                        if (c == '\'') {
                            // string (single-quoted) end
                            // System.out.println("lexed str '" + buffer + "'");
                            mode = LexMode.NORMAL;

                            tokens.add(new LexedToken<String>(
                                    LexedToken.Type.CONSTANT,
                                    buffer.toString()
                            ));

                            buffer = new StringBuilder();
                        } else {
                            buffer.append(c);
                        }
                        break;

                    case COMMENT:
                        // comment end
                        if (c == '\n') {
                            mode = LexMode.NORMAL;
                        }
                        break;
                }
            }
        }

        tokens.add(new LexedToken(LexedToken.Type.CODE_SEP));
    }

    LexedBlock() {}

    public String toString() {
        return tokens.stream()
                .map(LexedToken::toString)
                .collect(Collectors.joining("\n"));
    }
}
