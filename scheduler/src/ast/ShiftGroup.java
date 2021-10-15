package ast;

import validate.ProgramValidationException;

import java.util.List;

public class ShiftGroup extends Node {

    private final String name;
    private final List<String> shifts;
    
    public ShiftGroup(String name, List<String> shifts) {
        this.name = name;
        this.shifts = shifts;
    }

    public String getName() {
        return name;
    }

    public List<String> getShifts() {
        return shifts;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
