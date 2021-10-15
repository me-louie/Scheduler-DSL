package ast;

import validate.ProgramValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Shift extends Node {

    private final String name;
    private final LocalDateTime begin, end;

    public String getName() {
        return name;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    private static final DateTimeFormatter dateFormatter =
            new DateTimeFormatterBuilder().appendPattern("[MM/dd/yyyy HH:mm]").appendPattern("[MM-dd-yyyy HH:mm]")
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public Shift(String name, String open, String close) {
        this.name = name;
        this.begin = this.parseDateTime(open);
        this.end = this.parseDateTime(close);
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString.trim(), dateFormatter);
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
