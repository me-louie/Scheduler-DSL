package ast;

import ast.transformation.*;
import evaluate.ScheduledEvent;
import validate.NameNotFoundException;
import validate.ProgramValidationException;
import validate.ResultNotFound;
import validate.Validator;

import java.util.*;
import java.util.stream.Collectors;

public class SchedulerEvaluator implements SchedulerVisitor<Void> {

    // map of entity name to a list of all their scheduled events
    public Map<String, Set<ScheduledEvent>> scheduleMap = new HashMap<>();
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
        ShiftGroup sgResult = mergeHelper(m);
        program.shiftGroupMap.put(sgResult.getName(),sgResult);
        program.getShiftGroups().add(sgResult);
        return null;
    }

    private ShiftGroup mergeHelper(Merge m) {
        // create a new shift group
        List<String> result = null;
        String name = m.getName();
        String ShiftGroupNameOrM1 = m.getNameSGS1();
        String ShiftGroupNameOrMergeName = m.getNameSGS2();
        LogicalOperator lo = m.getlO();
        //Checking if the second name was a merge name
        boolean isSG1 = false;
        boolean isSG2 = false;
        if (program.shiftGroupMap.containsKey(ShiftGroupNameOrM1)){
            isSG1 = true;
        }
        if (program.shiftGroupMap.containsKey(ShiftGroupNameOrMergeName)){
            isSG2 = true;
        }
        boolean isMergeName1 = false;
        boolean isMergeName2 = false;
        Merge mergeObject1 = null;
        Merge mergeObject2 = null;
        for (Merge x: program.getMergeList()){
            if((x.getName()).equals(ShiftGroupNameOrM1)){
                isMergeName1 = true;
                mergeObject1 =x;
            }
            if((x.getName()).equals(ShiftGroupNameOrMergeName)){
                isMergeName2 = true;
                mergeObject2 =x;
            }

        }
        if(isSG1 && isSG2){
            List<String> shiftNamesSG1 = program.shiftGroupMap.get(ShiftGroupNameOrM1).getShiftList();
            System.out.println(shiftNamesSG1);
            List<String> shiftNamesSG2 = program.shiftGroupMap.get(ShiftGroupNameOrMergeName).getShiftList();
            if(lo.equals(LogicalOperator.AND)){
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2);
                System.out.println(shiftNamesSG1);
                if (!shiftNamesSG1.isEmpty()) {
                    result =  new ArrayList<>(set1);
                    System.out.println(result);
                    System.out.println(shiftNamesSG1+" Changed?");//Union of results.
                }else{
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
                System.out.println(program.shiftGroupMap.get(ShiftGroupNameOrM1).getShiftList());
                System.out.println(shiftNamesSG2);
                System.out.println(result);
                //Collections.sort(result); Maybe want to sort results
            }else if (lo.equals(LogicalOperator.XOR)){
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2); //set1 is an intersection
                set.removeAll(set1);
                result = new ArrayList<>(set);
                System.out.println(program.shiftGroupMap.get(ShiftGroupNameOrM1).getShiftList());
                System.out.println(shiftNamesSG1);
                System.out.println(result + " Result");
            }else{
                //TODO: Maybe throw operator not found exception.
            }
        }
        else if (isMergeName2 && !isMergeName1 && isSG1){
            List<String> shiftNamesSG1 = program.shiftGroupMap.get(ShiftGroupNameOrM1).getShiftList();
            ShiftGroup sg2 = mergeHelper(mergeObject2);
            List<String> shiftNamesSG2 = sg2.getShiftList();
            if(lo.equals(LogicalOperator.AND)){
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2);
                System.out.println(shiftNamesSG1);
                if (!shiftNamesSG1.isEmpty()) {
                    result =  new ArrayList<>(set1);
                    System.out.println(result);
                    System.out.println(shiftNamesSG1+" Changed?");//Union of results.
                }else{
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {

                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
            }else if (lo.equals(LogicalOperator.XOR)){
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2); //set1 is an intersection
                set.removeAll(set1);
                result = new ArrayList<>(set);
            }else{
                //TODO: Maybe throw operator not found exception.
            }
        }  else if (isMergeName1 && !isMergeName2 && isSG2){
            ShiftGroup sg1 = mergeHelper(mergeObject1);
            List<String> shiftNamesSG1 = sg1.getShiftList();
            List<String> shiftNamesSG2 = program.shiftGroupMap.get(ShiftGroupNameOrMergeName).getShiftList();
            if(lo.equals(LogicalOperator.AND)){
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2);
                System.out.println(shiftNamesSG1);
                if (!shiftNamesSG1.isEmpty()) {
                    result =  new ArrayList<>(set1);
                    System.out.println(result);
                    System.out.println(shiftNamesSG1+" Changed?");//Union of results.
                }else{
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
            }else if (lo.equals(LogicalOperator.XOR)){
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2); //set1 is an intersection
                set.removeAll(set1);
                result = new ArrayList<>(set);
            }else{
                //TODO: Maybe throw operator not found exception.
            }
        } else if(isMergeName1 && isMergeName2) {
            ShiftGroup sg1 = mergeHelper(mergeObject1);
            List<String> shiftNamesSG1 = sg1.getShiftList();
            ShiftGroup sg2 = mergeHelper(mergeObject2);
            List<String> shiftNamesSG2 = sg2.getShiftList();
            if(lo.equals(LogicalOperator.AND)){
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2);
                System.out.println(shiftNamesSG1);
                if (!shiftNamesSG1.isEmpty()) {
                    result =  new ArrayList<>(set1);
                    System.out.println(result);
                    System.out.println(shiftNamesSG1+" Changed?");//Union of results.
                }else{
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
            }else if (lo.equals(LogicalOperator.XOR)){
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2); //set1 is an intersection
                set.removeAll(set1);
                result = new ArrayList<>(set);
            }else{
                //TODO: Maybe throw operator not found exception.
            }
        }
        else{
            throw new NameNotFoundException("Shift Group or Merge Name not found");
        }
        return new ShiftGroup(name, result);
    }

    @Override
    public Void visit(Loop l) throws ProgramValidationException {
        validator.validate(l);

        List<String> entityList = program.entityGroupMap.get(l.getNameEEG()).getEntities();
        List<Entity> entities = program.entityMap.entrySet().stream().filter(e -> entityList.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        List<String> shiftList = program.shiftGroupMap.get(l.getNameSSG()).getShiftList();
        List<Shift> shifts = program.shiftMap.entrySet().stream().filter(e -> shiftList.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        Integer days = 0;
        Integer repeat = 0;
        while (repeat < l.getRepNum()) {

            for (Entity e : entities) {

                for (Shift s : shifts) {

                    ScheduledEvent scheduledEvent = new ScheduledEvent(s.getOpen().plusDays(days),
                                                                        s.getClose().plusDays(days),
                                                                        s.getName());
                    if (!scheduleMap.containsKey(e.getName())) {
                        scheduleMap.put(e.getName(), new HashSet<>());
                    }
                    scheduleMap.get(e.getName()).add(scheduledEvent);
                }

                if (l.getB0() == BitwiseOperator.RIGHTSHIFT) {
                    days += l.getNum();
                } else {
                    days -= l.getNum();
                }
            }
            repeat++;
        }

        return null;
    }

    void applyShiftToEntity(Shift shift, String entityName) {
        // todo: change LocalDateTime to Calendar from the get go so this works
        ScheduledEvent scheduledEvent = new ScheduledEvent(shift.getOpen(), shift.getClose(), shift.getName());
        if (!scheduleMap.containsKey(entityName)) {
            scheduleMap.put(entityName, new HashSet<>());
        }
        scheduleMap.get(entityName).add(scheduledEvent);
    }
}
