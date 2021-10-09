package ast;

import validate.ProgramValidationException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Shift extends Node {

    private final String name;
    private final LocalDateTime open, close;

    public String getName() {
        return name;
    }

    public LocalDateTime getOpen() {
        return open;
    }

    public LocalDateTime getClose() {
        return close;
    }

    private static final DateTimeFormatter dateFormatter =
            new DateTimeFormatterBuilder().appendPattern("[MM/dd/yyyy HH:mm]").appendPattern("[MM-dd-yyyy HH:mm]")
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public Shift(String name, String open, String close) {
        this.name = name;
        this.open = this.parseDateTime(open);
        this.close = this.parseDateTime(close);
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString.trim(), dateFormatter);
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
