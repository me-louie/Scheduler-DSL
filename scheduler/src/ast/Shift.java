package ast;

import validate.ProgramValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Shift extends Node {

    private static final DateTimeFormatter dateFormatter =
            new DateTimeFormatterBuilder().appendPattern("[MM/dd/yyyy HH:mm]").appendPattern("[MM-dd-yyyy HH:mm]")
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
    private final String name;
    private final LocalDateTime open, close;
    private final String description;

    public Shift(String name, String open, String close, String description) {
        this.name = name;
        this.open = this.parseDateTime(open);
        this.close = this.parseDateTime(close);
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getOpen() {
        return open;
    }

    public LocalDateTime getClose() {
        return close;
    }

    public String getDescription() {
        return description;
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString.trim(), dateFormatter);
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
