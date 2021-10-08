package ast.transformations;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.DayOfWeek;
import java.util.List;

import ast.math.Function;

public class Frequency extends Rule {

    private final String name;
    private int fMoreThan;
    private Function func;
    private final List<DayOfWeek> days;

    public Frequency (String name, String fMorethan, List<DayOfWeek> days) throws NumberFormatException {
        //Note: Lists made in the PareToAst class. 
        this.name = name;
        this.fMoreThan = Integer.parseInt(fMorethan);
        this.days = days;
    }

    public Frequency (String name, Function func, List<DayOfWeek> days) throws NumberFormatException {
        //Note: Lists made in the ParseToAst class. 
        this.name = name;
        // TOD: Change when we know how to handle variables
        this.fMoreThan = 0;
        this.func = func;
        this.days = days;
    }

    public String getName() {
      return name;
    }

    public List<DayOfWeek> getDays() {
      return days;
    }

    public Function getFunc() {
      return func;
    }

    public int getFMoreThan() {
      return fMoreThan;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
