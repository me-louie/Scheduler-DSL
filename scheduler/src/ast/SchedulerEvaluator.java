package ast;

import ast.transformation.*;
import evaluate.ScheduledEvent;
import validate.ProgramValidationException;
import validate.Validator;

import java.util.*;

public class SchedulerEvaluator implements SchedulerVisitor<Void> {

    // map of entity name to a list of all their scheduled events
    public Map<String, Collection<ScheduledEvent>> scheduleMap = new HashMap<>();
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
        // we don't actually use this for anything
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
        // no evaluation
        return null;
    }

    @Override
    public Void visit(ShiftGroup sg) throws ProgramValidationException {
        validator.validate(sg);
        // no evaluation
        return null;
    }

    @Override
    public Void visit(Apply a) throws ProgramValidationException {
        validator.validate(a);

        String entityOrEntityGroupName = a.getNameEEG();
        String shiftOrShiftGroupName = a.getNameSGMG();
        boolean isEntity = program.entityMap.containsKey(entityOrEntityGroupName);
        boolean isShift = program.shiftMap.containsKey(shiftOrShiftGroupName);

        if (isEntity && isShift) { // is an entity and a shift
            applyShiftToEntity(program.shiftMap.get(shiftOrShiftGroupName), entityOrEntityGroupName);
        } else if (isEntity && !isShift){ // is an entity and a shift group
            for (String shiftName : program.shiftGroupMap.get(shiftOrShiftGroupName).getShiftList()) {
                applyShiftToEntity(program.shiftMap.get(shiftName), entityOrEntityGroupName);
            }
        } else if (!isEntity && isShift) { // is an entity group and a shift
            Shift shift = program.shiftMap.get(shiftOrShiftGroupName);
            for (String entityName : program.entityGroupMap.get(entityOrEntityGroupName).getEntities()) {
                applyShiftToEntity(shift, entityName);
            }
        } else { // is an entity group and a shift group
            for (String entityName : program.entityGroupMap.get(entityOrEntityGroupName).getEntities()) {
                for (String shiftName : program.shiftGroupMap.get(shiftOrShiftGroupName).getShiftList()) {
                    applyShiftToEntity(program.shiftMap.get(shiftName), entityName);
                }
            }
        }
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

    private void applyShiftToEntity(Shift shift, String entityName) {
        ScheduledEvent scheduledEvent = new ScheduledEvent(shift.getOpen(), shift.getClose(), shift.getName());
        if (!scheduleMap.containsKey(entityName)) {
            scheduleMap.put(entityName, new HashSet<>());
        }
        scheduleMap.get(entityName).add(scheduledEvent);
    }


}
