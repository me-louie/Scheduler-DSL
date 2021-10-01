package ast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

public class Range extends Node {
    private final LocalDate start, end;

    public Range(String startDate, String endDate) {
        this.start = Helper.parseDateString(startDate);
        this.end = Helper.parseDateString(endDate);
    }

    public LocalDate getStart() {
      return start;
    }

    public LocalDate getEnd() {
      return end;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
