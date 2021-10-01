package ui;

import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import ast.Program;
import parser.ParseToASTVisitor;
import parser.SchedulerLexer;
import parser.SchedulerParser;

public class Main {
    public static void main(String[] args) throws IOException {
        SchedulerLexer lexer = new SchedulerLexer(CharStreams.fromFileName("Examples.txt"));

        for (Token token : lexer.getAllTokens()) {
            System.out.println(token);
        }
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        System.out.println("Done tokenizing");

        SchedulerParser parser = new SchedulerParser(tokens);
        System.out.println("test");
        ParseToASTVisitor visitor = new ParseToASTVisitor();
        //SchedulerParser.ProgramContext a = parser.program();
        System.out.println("test2");
        Program parsedProgram = visitor.visitProgram(parser.program());
        System.out.println("Done parsing");
        System.out.println(parsedProgram.shouldScheduleAllHours());
    }
}
