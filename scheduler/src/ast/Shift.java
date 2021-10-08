package ast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Shift extends Node {

    private final String name;

    private final LocalDate date;

    private final LocalTime open, close;

    public Shift(String name, LocalDate date, LocalTime open, LocalTime close) {
        this.name = name;
        this.date = date;
        this.open = open;
        this.close = close;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
