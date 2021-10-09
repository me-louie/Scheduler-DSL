package ast;

import ast.transformation.*;
import evaluate.ScheduledEvent;
import validate.ProgramValidationException;
import validate.Validator;

import java.util.ArrayList;
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
        boolean isEntity = program.getEntities().containsKey(entityOrEntityGroupName);
        boolean isShift = program.getShifts().containsKey(shiftOrShiftGroupName);
        if (isEntity && isShift) { // is an entity and a shift
            applyShiftToEntity(program.getShifts().get(shiftOrShiftGroupName), entityOrEntityGroupName);
        } else if (isEntity && !isShift){ // is an entity and a shift group
            // get the shift group
            // for each shift in group apply shifttoentity(shift, entityName);
            for (Shift shift : program.getShiftGroups().get(shiftOrShiftGroupName)) {
                applyShiftToEntity(shift, entityOrEntityGroupName);
            }
        } else if (!isEntity && isShift) { // is an entity group and a shift
            Shift shift = program.getShifts().get(shiftOrShiftGroupName);
            for (Entity entity : program.getEntityGroups().get(entityOrEntityGroupName)) {
                applyShiftToEntity(shift, entity.getName());
            }
        } else { // is an entity group and a shift
            for (Entity entity : program.getEntityGroups().get(entityOrEntityGroupName)) {
                for (Shift shift : program.getShiftGroups().get(shiftOrShiftGroupName)) {
                    applyShiftToEntity(shift, entity.getName());
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

    void applyShiftToEntity(Shift shift, String entityName) {
        // todo: change LocalDateTime to Calendar from the get go so this works
        ScheduledEvent scheduledEvent = new ScheduledEvent(shift.getOpen(), shift.getClose(), shift.getName());
        if (!scheduleMap.containsKey(entityName)) {
            scheduleMap.put(entityName, new ArrayList<>());
        }
        scheduleMap.get(entityName).add(scheduledEvent);
    }


}
