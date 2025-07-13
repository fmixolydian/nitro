package eu.sqrt5.nitro.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataBlock {
    Map<String, Word> values = new HashMap<>();

    DataBlock() {

    }

    DataBlock(LexedBlock block) throws SyntaxError {
        Iterator<LexedToken> iterator = block.tokens.iterator();
        while (iterator.hasNext()) {
            LexedToken<String> key;
            LexedToken         token;
            Word value;

            key = LexedBlock.next_guarded(iterator, LexedToken.Type.LITERAL, LexedToken.Type.CODE_SEP);
            if (key == null) break;

            LexedBlock.next_guarded(iterator, LexedToken.Type.DATA_SEP);
            token = iterator.next();

            switch (token.type) {
                case CODE_START:
                    value = new Word<>(
                            Word.Type.CODE_BLOCK,
                            new CodeBlock(LexedBlock.capture_until(iterator,
                                    LexedToken.Type.CODE_START,
                                    LexedToken.Type.CODE_END))
                    );
                    break;
                case EXPR_START:
                    value = new Word<>(
                            Word.Type.EXPR_BLOCK,
                            new CodeBlock(LexedBlock.capture_until(iterator,
                                    LexedToken.Type.EXPR_START,
                                    LexedToken.Type.EXPR_END))
                    );
                    break;
                case DATA_START:
                    value = new Word<>(
                            Word.Type.DATA_BLOCK,
                            new DataBlock(LexedBlock.capture_until(iterator,
                                    LexedToken.Type.DATA_START,
                                    LexedToken.Type.DATA_END))
                    );
                    break;

                case CODE_SEP:
                    throw new SyntaxError("Expected value", token.line_no, token.line_ch);
                case DATA_SEP:
                    throw new SyntaxError("Unexpected DATA SEPARATOR in data value", token.line_no, token.line_ch);
                case CODE_END:
                    throw new SyntaxError("Expected '{' to start code block", token.line_no, token.line_ch);
                case DATA_END:
                    throw new SyntaxError("Expected '(' to start data block", token.line_no, token.line_ch);
                case EXPR_END:
                    throw new SyntaxError("Expected '[' to start expression", token.line_no, token.line_ch);

                default:
                    value = new Word(token);
                    break;
            }

            values.put(key.value, value);
        }
    }

    /*
    public void dump(int indent) {
        for (var entry : values.entrySet()) {
            System.out.print((entry.getKey() + " =").indent(indent).stripTrailing());
            System.out.print(" ");
            entry.getValue().dump(indent + 1, false);
            System.out.println();
        }
    }
     */
}
