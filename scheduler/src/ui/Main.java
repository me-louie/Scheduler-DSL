package ui;

import java.io.IOException;
import java.util.*;

import ast.SchedulerEvaluator;
import ast.ShiftGroup;
import ast.transformation.LogicalOperator;
import ast.transformation.Merge;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import ast.Program;
import output.OutputGenerator;
import parser.ParseToASTVisitor;
import parser.SchedulerLexer;
import parser.SchedulerParser;
import validate.NameNotFoundException;
import validate.ProgramValidationException;
import validate.ResultNotFound;

public class Main {
    public static void main(String[] args) throws IOException, ProgramValidationException {
        SchedulerLexer lexer = new SchedulerLexer(CharStreams.fromFileName("Example2.txt"));

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
