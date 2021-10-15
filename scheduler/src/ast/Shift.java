package ast;

import validate.ProgramValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Shift extends Node {

    private final String name;
    private final LocalDateTime begin, end;
    private final String description;

    private static final DateTimeFormatter dateFormatter =
            new DateTimeFormatterBuilder().appendPattern("[MM/dd/yyyy HH:mm]").appendPattern("[MM-dd-yyyy HH:mm]")
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();

    public Shift(String name, String open, String close, String description) {
        this.name = name;
        this.begin = this.parseDateTime(open);
        this.end = this.parseDateTime(close);
        this.description = description;
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString.trim(), dateFormatter);
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
