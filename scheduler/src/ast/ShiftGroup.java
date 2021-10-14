package ast;

import validate.ProgramValidationException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ShiftGroup extends Node {

    private final String name;
    private final List<String> shiftList;
    
    public ShiftGroup(String name, List<String> shiftList) {
        this.name = name;
        this.shiftList = shiftList;
    }

    public String getName() {
        return name;
    }

    public List<String> getShiftList() {
        return shiftList;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
