package ast;

import ast.transformation.*;
import evaluate.ScheduledEvent;
import validate.ProgramValidationException;
import validate.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulerEvaluator implements SchedulerVisitor<Void> {

    // map of entity name to a list of all their scheduled events
    Map<String, List<ScheduledEvent>> scheduleMap = new HashMap<>();
    Program program;
    Validator validator;

    @Override
    public Void visit(Program p) throws ProgramValidationException {
        program = p;
        validator = new Validator(p);
        p.getHeader().accept(this);
        p.getEntities().forEach(e -> e.accept(this));
        p.getEntityGroups().forEach(eg -> eg.accept(this));
        p.getShifts().forEach(s -> s.accept(this));
        p.getShiftGroups().forEach(sg -> sg.accept(this));
        p.getTransformations().forEach(t -> t.accept(this));
        return null;
    }

    @Override
    public Void visit(Header h) {
        // in each visit do
        //  - basic validation (use global program to do necessary checks)
        //  - add an entry to scheduleMap if is an apply or loop
        //  - create a new ShiftGroup if is a merge
        return null;
    }

    @Override
    public Void visit(Entity e) throws ProgramValidationException {
        validator.validate(e);
        // no evaluation
        return null;
    }

    @Override
    public Void visit(EntityGroup eg) throws ProgramValidationException {
        validator.validate(eg);
        // no evaluation
        return null;
    }

    @Override
    public Void visit(Shift s) throws ProgramValidationException {
        validator.validate(s);
        // no evalution
        return null;
    }

    @Override
    public Void visit(ShiftGroup sg) throws ProgramValidationException {
        validator.validate(sg);
        return null;
    }

    @Override
    public Void visit(Apply a) throws ProgramValidationException {
        validator.validate(a);
        // add entry to scheduleMap
        return null;
    }

    @Override
    public Void visit(Merge m) throws ProgramValidationException {
        validator.validate(m);
        // create a new shift group
        return null;
    }

    @Override
    public Void visit(Loop l) throws ProgramValidationException {
        validator.validate(l);
        // add a bunch of nodes to scheduleMap
        return null;
    }

    @Override
    public Void visit(LogicalOperator lo) {
        return null;
    }

    @Override
    public Void visit(BitwiseOperator la) {
        return null;
    }


}
