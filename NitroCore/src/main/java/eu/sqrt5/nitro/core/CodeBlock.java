package eu.sqrt5.nitro.core;

import java.util.*;

public class CodeBlock {
    private List<Command> commands = new ArrayList<>();

    public CodeBlock(String string) throws SyntaxError {
        this(new LexedBlock(string));
    }

    public CodeBlock(LexedBlock block) throws SyntaxError {
        var iter = block.tokens.iterator();

        List<Word> words = new ArrayList<>();

        while (iter.hasNext()) {
            LexedToken token = iter.next();

            switch (token.type) {
                case CODE_START:
                    words.add(new Word<>(
                            Word.Type.CODE_BLOCK,
                            new CodeBlock(LexedBlock.capture_until(iter, LexedToken.Type.CODE_START, LexedToken.Type.CODE_END))
                    ));
                    break;

                case EXPR_START:
                    words.add(new Word<>(
                            Word.Type.EXPR_BLOCK,
                            new CodeBlock(LexedBlock.capture_until(iter, LexedToken.Type.EXPR_START, LexedToken.Type.EXPR_END))
                    ));
                    break;

                case DATA_START:
                    words.add(new Word<>(
                            Word.Type.DATA_BLOCK,
                            new DataBlock(LexedBlock.capture_until(iter, LexedToken.Type.DATA_START, LexedToken.Type.DATA_END))
                    ));
                    break;

                case CODE_SEP:
                    Word commandName = words.removeFirst();

                    if (commandName.type != Word.Type.LITERAL) {
                        throw new SyntaxError("Expected literal name at start of command", commandName.line_no, commandName.line_ch);
                    }
                    commands.add(new Command((String) commandName.value, words));
                    words = new ArrayList<>();
                    break;

                case DATA_SEP:
                    throw new SyntaxError("Unexpected DATA SEPARATOR in code block", token.line_no, token.line_ch);
                case CODE_END:
                    throw new SyntaxError("Expected '{' to start code block", token.line_no, token.line_ch);
                case DATA_END:
                    throw new SyntaxError("Expected '(' to start data block", token.line_no, token.line_ch);
                case EXPR_END:
                    throw new SyntaxError("Expected '[' to start expression", token.line_no, token.line_ch);

                default:
                    words.add(new Word(token));
                    break;

            }
        }
    }

    public Object exec(Nitro nitro, Namespace local) {
        for (Command command : commands) {
            Object result = command.exec(local);
            if (command == commands.getLast()) {
                return result;
            }
        }

        return null;
    }
}
