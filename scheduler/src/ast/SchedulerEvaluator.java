package ast;

import ast.transformation.*;
import evaluate.ScheduledEvent;
import validate.NameNotFoundException;
import validate.ProgramValidationException;
import validate.ResultNotFound;
import validate.Validator;

import java.util.*;

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
        boolean isEntity = program.entityMap.containsKey(entityOrEntityGroupName);
        boolean isShift = program.shiftMap.containsKey(shiftOrShiftGroupName);
        if (isEntity && isShift) { // is an entity and a shift
            applyShiftToEntity(program.shiftMap.get(shiftOrShiftGroupName), entityOrEntityGroupName);
        } else if (isEntity && !isShift){ // is an entity and a shift group
            // get the shift group
            // for each shift in group apply shifttoentity(shift, entityName);
            for (Shift shift : program.getShiftGroups().get(shiftOrShiftGroupName)) {
                applyShiftToEntity(shift, entityOrEntityGroupName);
            }
        } else if (!isEntity && isShift) { // is an entity group and a shift
            Shift shift = program.shiftMap.get(shiftOrShiftGroupName);
            for (Entity entity : program.entityMap.get(entityOrEntityGroupName)) {
                applyShiftToEntity(shift, entity.getName());
            }
        } else { // is an entity group and a shift
            for (Entity entity : program.entityGroupMap.get(entityOrEntityGroupName)) {
                for (Shift shift : program.shiftMap.get(shiftOrShiftGroupName)) {
                    applyShiftToEntity(shift, entity.getName());
                }
            }
        }
        return null;
    }

    @Override
    public Void visit(Merge m) throws ProgramValidationException {
        validator.validate(m);
        ShiftGroup sgResult = mergeHelper(m);

        return null;
    }

    private ShiftGroup mergeHelper(Merge m) {
        // create a new shift group
        List<String> result = null;
        String name = m.getName() + "ShiftGroup";
        String shiftOrshiftGroupName1 = m.getNameSGS1();
        String shiftOrShiftGroupNameOrMergeName = m.getNameSGS2();
        LogicalOperator lo = m.getlO();
        //Checking if the second name was a merge name
        boolean isMergeName = false;
        Merge mergeObject = null;
        for (Merge x: program.getMergeList()){
            if(x.getName().equals(shiftOrShiftGroupNameOrMergeName)){
                isMergeName = true;
                mergeObject =x;
            }
        }
        if(program.shiftGroupMap.containsKey(shiftOrShiftGroupNameOrMergeName)){
            if(lo.equals(LogicalOperator.AND)){
                List<String> shiftNamesSG1 = program.shiftGroupMap.get(shiftOrshiftGroupName1).getShiftList();
                List<String> shiftNamesSG2 = program.shiftGroupMap.get(shiftOrShiftGroupNameOrMergeName).getShiftList();
                System.out.println(shiftNamesSG1);
                if (shiftNamesSG1.retainAll(shiftNamesSG2)) {
                    result = shiftNamesSG1;
                    System.out.println(shiftNamesSG1+" Changed?");//Union of results.
                }else{
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {
                List<String> shiftNamesSG1 = program.shiftGroupMap.get(shiftOrshiftGroupName1).getShiftList();
                List<String> shiftNamesSG2 = program.shiftGroupMap.get(shiftOrShiftGroupNameOrMergeName).getShiftList();
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
            }else if (lo.equals(LogicalOperator.XOR)){
                List<String> shiftNamesSG1 = program.shiftGroupMap.get(shiftOrshiftGroupName1).getShiftList();
                List<String> shiftNamesSG2 = program.shiftGroupMap.get(shiftOrShiftGroupNameOrMergeName).getShiftList();
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                shiftNamesSG1.retainAll(shiftNamesSG2); //shiftNamesSG1 is intersection
                set.removeAll(shiftNamesSG1);
                result = new ArrayList<>(set);
            }else{
                //TODO: Maybe throw operator not found exception.
            }
        }
        else if (isMergeName){
            if(lo.equals(LogicalOperator.AND)){
                List<String> shiftNamesSG1 = program.shiftGroupMap.get(shiftOrshiftGroupName1).getShiftList();
                ShiftGroup sg2 = mergeHelper(mergeObject);
                List<String> shiftNamesSG2 = sg2.getShiftList();
                System.out.println(shiftNamesSG1);
                if (shiftNamesSG1.retainAll(shiftNamesSG2)) {
                    result = shiftNamesSG1;
                    System.out.println(shiftNamesSG1+" Changed?");//Union of results.
                }else{
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {
                List<String> shiftNamesSG1 = program.shiftGroupMap.get(shiftOrshiftGroupName1).getShiftList();
                ShiftGroup sg2 = mergeHelper(mergeObject);
                List<String> shiftNamesSG2 = sg2.getShiftList();
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
            }else if (lo.equals(LogicalOperator.XOR)){
                List<String> shiftNamesSG1 = program.shiftGroupMap.get(shiftOrshiftGroupName1).getShiftList();
                ShiftGroup sg2 = mergeHelper(mergeObject);
                List<String> shiftNamesSG2 = sg2.getShiftList();
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                shiftNamesSG1.retainAll(shiftNamesSG2); //shiftNamesSG1 is intersection
                set.removeAll(shiftNamesSG1);
                result = new ArrayList<>(set);
            }else{
                //TODO: Maybe throw operator not found exception.
            }
        }
        else{
            throw new NameNotFoundException("Shift Group Name not found");
        }
        return new ShiftGroup(name, result);
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
