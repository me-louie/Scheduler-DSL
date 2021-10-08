package ast.transformations;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.LocalDate;

import ast.Helper;
import org.antlr.v4.tool.Rule;

public class Availability extends Transformations {

    private final String name;
    private final LocalDate dStart, dEnd;
    private final LocalTime tStart, tEnd;

    public Availability(String name, String dStart, String dEnd, String tStart, String tEnd) throws ParseException {
        this.tStart = LocalTime.parse(tStart);
        this.tEnd = LocalTime.parse(tEnd);
        this.dStart = Helper.parseDateString(dStart);
        this.dEnd = Helper.parseDateString(dEnd);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getStartDate() {
        return this.dStart;
    }

    public LocalDate getEndDate() {
        return this.dEnd;
    }

    public LocalTime getStartTime() {
        return tStart;
    }

    public LocalTime getEndTime(){
        return tEnd;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
