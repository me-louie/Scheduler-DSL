package ui;

import java.io.IOException;

import ast.SchedulerEvaluator;
import org.antlr.v4.runtime.*;

import ast.Program;
import output.OutputGenerator;
import parser.ParseToASTVisitor;
import parser.SchedulerLexer;
import parser.SchedulerParser;
import validate.ProgramValidationException;

public class Main {
    public static void main(String[] args) throws IOException, ProgramValidationException {
        // Path for example input files: "scheduler/ExampleInputs/BasicExample.txt"
        SchedulerLexer lexer = new SchedulerLexer(CharStreams.fromFileName("DemoExample.txt"));
        for (Token token : lexer.getAllTokens()) {
            System.out.println(token);
        }
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        System.out.println("Done tokenizing");
        SchedulerParser parser = new SchedulerParser(tokens);
        ParseToASTVisitor visitor = new ParseToASTVisitor();
        Program parsedProgram = visitor.visitProgram(parser.program());
        System.out.println("Done parsing");
        SchedulerEvaluator schedulerEvaluator = new SchedulerEvaluator();
        parsedProgram.accept(schedulerEvaluator);
        System.out.println("Done scheduling");
        OutputGenerator og = new OutputGenerator();
        og.generate(schedulerEvaluator.scheduleMap, "mycalendar.ics");
        System.out.println("Done outputting");
    }
}
