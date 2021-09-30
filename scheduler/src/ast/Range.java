package ast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

public class Range extends Node {
    private final Date start, end;

    public Range(String startDate, String endDate) throws ParseException {
        this.start = Helper.parseDateString(startDate);
        this.end = Helper.parseDateString(endDate);
    }

    public Date getStart() {
      return start;
    }

    public Date getEnd() {
      return end;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
