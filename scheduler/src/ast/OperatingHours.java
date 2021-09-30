package ast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;

public class OperatingHours extends Node {
    private final LocalTime open, close;

    public OperatingHours(String open, String close) {
        // LocalTime.parse throws DateTimeParseException
        this.open = LocalTime.parse(open);
        this.close = LocalTime.parse(close);
    }

    public LocalTime getOpen() {
        return open;
    }

    public LocalTime getClose() {
        return close;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
