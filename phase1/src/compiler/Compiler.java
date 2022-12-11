package compiler;

import gen.ToorlaLexer;
import gen.ToorlaListener;
import gen.ToorlaParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class Compiler {
    public static void main(String[] args) throws IOException {
        CharStream stream = CharStreams.fromFileName("sample/input.trl");
        ToorlaLexer lexer = new ToorlaLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        ToorlaParser parser = new ToorlaParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        ToorlaListener listener = new ProgramPrinter();
        walker.walk(listener, tree);
    }
}
