package ast;

import ast.math.Function;
import ast.math.MathOP;
import ast.math.Var;
import ast.transformation.*;
import evaluate.ScheduledEvent;
import validate.NameNotFoundException;
import validate.ProgramValidationException;
import validate.ResultNotFound;
import validate.Validator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SchedulerEvaluator implements SchedulerVisitor<Void> {

    // map of entity name to a set of all their scheduled events
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

    private Integer varVal(Var var) {
        Integer value = null;
        System.out.println(var.getName2() + " IN VAR VAL");
        if (var.getName2() != null) {
            if (program.varMaps.containsKey(var.getName2())) {
                value = varVal(program.varMaps.get(var.getName2()));
            } else {
                throw new NameNotFoundException(var.getName2() + " var name not present");
            }
        } else {
            value = var.getNum();
        }
        return value;
    }

    private Integer getMathVal(Integer num1, Integer num2, MathOP mathOP) {
        Integer result = null;
        return switch (mathOP) {
            case PLUS -> result = num1 + num2;
            case MINUS -> result = num1 - num2;
            case MULTIPLY -> result = num1 * num2;
            case DIVIDE -> result = num1 / num2;
            case POWER -> result = (int) Math.pow(num1, num2);
            default -> throw new RuntimeException("Unrecognized MathOP");
        };
    }

    private Integer varOrfuncCheckHelper(String name) {
        Integer result = null;
        System.out.println(name);
        if (program.varMaps.containsKey(name)) {
            result = varVal(program.varMaps.get(name));
        } else if (program.functionMap.containsKey(name)) {
            result = funcVal(program.functionMap.get(name));
        } else {
            throw new NameNotFoundException(name + " VAR OR FUNCTION NAME not present");
        }

        return result;
    }

    private Integer funcVal(Function func) {
        Integer value = null;
        Integer num1 = null;
        Integer num2 = null;
        MathOP mathOP = func.mathOP;
        if (func.getNum1() != null && func.getNum2() != null) {
            num1 = func.getNum1();
            num2 = func.getNum2();
            value = getMathVal(num1, num2, mathOP);
            System.out.println(value);
        } else if (func.getNum1() == null && func.getVarOrFuncName1() != null && func.getNum2() != null) {
            num1 = varOrfuncCheckHelper(func.getVarOrFuncName1());
            num2 = func.getNum2();
            value = getMathVal(num1, num2, mathOP);
            System.out.println(value);
        } else if (func.getNum1() != null && func.getNum2() == null && func.getVarOrFuncName2() != null) {
            num2 = varOrfuncCheckHelper(func.getVarOrFuncName2());
            num1 = func.getNum1();
            value = getMathVal(num1, num2, mathOP);
            System.out.println(value);
        } else if (func.getNum1() == null && func.getVarOrFuncName1() != null && func.getNum2() == null && func.getVarOrFuncName2() != null) {
            num1 = varOrfuncCheckHelper(func.getVarOrFuncName1());
            num2 = varOrfuncCheckHelper(func.getVarOrFuncName2());
            value = getMathVal(num1, num2, mathOP);
            System.out.println(value);
        }
        return value;
    }

    @Override
    public Void visit(Apply a) throws ProgramValidationException {
        validator.validate(a);

        String entityOrEntityGroupName = a.getNameEEG();
        String shiftOrShiftGroupName = a.getNameSGMG();
        boolean isEntity = program.entityMap.containsKey(entityOrEntityGroupName);
        boolean isShift = program.shiftMap.containsKey(shiftOrShiftGroupName);
        BitwiseOperator b0 = a.getbO();
        Integer num = a.getNum();
        if (a.getVarOrFunc() != null) {
            num = varOrfuncCheckHelper(a.getVarOrFunc());
        }
        System.out.println(num + " this num in evaluator apply");
        TimeUnit tU = a.getTimeUnit();

        if (isEntity && isShift) { // is an entity and a shift
            applyShiftToEntity(program.shiftMap.get(shiftOrShiftGroupName), entityOrEntityGroupName, b0, num, tU);
        } else if (isEntity && !isShift) { // is an entity and a shift group
            for (String shiftName : program.shiftGroupMap.get(shiftOrShiftGroupName).getShiftList()) {
                applyShiftToEntity(program.shiftMap.get(shiftName), entityOrEntityGroupName, b0, num, tU);
            }
        } else if (!isEntity && isShift) { // is an entity group and a shift
            Shift shift = program.shiftMap.get(shiftOrShiftGroupName);
            for (String entityName : program.entityGroupMap.get(entityOrEntityGroupName).getEntities()) {
                applyShiftToEntity(shift, entityName, b0, num, tU);
            }
        } else { // is an entity group and a shift group
            for (String entityName : program.entityGroupMap.get(entityOrEntityGroupName).getEntities()) {
                for (String shiftName : program.shiftGroupMap.get(shiftOrShiftGroupName).getShiftList()) {
                    applyShiftToEntity(program.shiftMap.get(shiftName), entityName, b0, num, tU);
                }
            }
        }
        return null;
    }

    @Override
    public Void visit(Merge m) throws ProgramValidationException {
        validator.validate(m);
        ShiftGroup sgResult = mergeHelper(m);
        program.shiftGroupMap.put(sgResult.getName(), sgResult);
        program.getShiftGroups().add(sgResult);
        return null;
    }

    @Override
    public Void visit(IfThenElse ifThenElse) throws ProgramValidationException {
        validator.validate(ifThenElse);
        // Evaluate the conditional
        ifThenElse.getCond().accept(this);
        boolean condValue = ifThenElse.getCond().getState();
        if (condValue) {
            for (Transformation t : ifThenElse.getThenTransformations()) {
                t.accept(this);
            }
        } else {
            for (Transformation t : ifThenElse.getElseTransformations()) {
                t.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visit(Cond cond) throws ProgramValidationException {
        LogicalOperator logicalOperator = cond.getOperator();
        String shiftOrShiftGroupName1 = cond.getNameSSG1();
        String shiftOrShiftGroupName2 = cond.getNameSSG2();

        Set<String> shiftGroup1 =
                program.shiftGroupMap.get(shiftOrShiftGroupName1).getShiftList().stream().collect(Collectors.toSet());
        Set<String> shiftGroup2 =
                program.shiftGroupMap.get(shiftOrShiftGroupName2).getShiftList().stream().collect(Collectors.toSet());
        Set<String> resultantShifts = new HashSet<>(shiftGroup1);

        if (logicalOperator == LogicalOperator.AND) {
            resultantShifts.stream().filter(shiftGroup2::contains).collect(Collectors.toSet());
        } else if (logicalOperator == LogicalOperator.OR) {
            resultantShifts.addAll(shiftGroup2);
        } else if (logicalOperator == LogicalOperator.XOR) {
            resultantShifts.addAll(shiftGroup2);
            shiftGroup1.retainAll(shiftGroup2);     //SG1 is the intersection
            resultantShifts.removeAll(shiftGroup1);
        }
        cond.setState(!resultantShifts.isEmpty());
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
        if (program.shiftGroupMap.containsKey(ShiftGroupNameOrM1)) {
            isSG1 = true;
        }
        if (program.shiftGroupMap.containsKey(ShiftGroupNameOrMergeName)) {
            isSG2 = true;
        }
        boolean isMergeName1 = false;
        boolean isMergeName2 = false;
        Merge mergeObject1 = null;
        Merge mergeObject2 = null;
        for (Merge x : program.getMergeList()) {
            if ((x.getName()).equals(ShiftGroupNameOrM1)) {
                isMergeName1 = true;
                mergeObject1 = x;
            }
            if ((x.getName()).equals(ShiftGroupNameOrMergeName)) {
                isMergeName2 = true;
                mergeObject2 = x;
            }

        }
        if (isSG1 && isSG2) {
            List<String> shiftNamesSG1 = program.shiftGroupMap.get(ShiftGroupNameOrM1).getShiftList();
            List<String> shiftNamesSG2 = program.shiftGroupMap.get(ShiftGroupNameOrMergeName).getShiftList();
            if (lo.equals(LogicalOperator.AND)) {
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2);
                System.out.println(shiftNamesSG1);
                if (!shiftNamesSG1.isEmpty()) {
                    result = new ArrayList<>(set1);
                    System.out.println(result);
                    System.out.println(shiftNamesSG1 + " Changed?");//Union of results.
                } else {
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
            } else if (lo.equals(LogicalOperator.XOR)) {
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
                System.out.println(shiftNamesSG2);
                System.out.println(result + " Result");
            } else if (lo.equals(LogicalOperator.EXCEPT)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2); //set1 is an intersection
                set.removeAll(set1);
                System.out.println(program.shiftGroupMap.get(ShiftGroupNameOrM1).getShiftList());
                System.out.println(shiftNamesSG1);
                System.out.println(shiftNamesSG2);
                result = new ArrayList<>(set);
                System.out.println(result);

            } else {
                //TODO: Maybe throw operator not found exception.
            }
        } else if (isMergeName2 && !isMergeName1 && isSG1) {
            List<String> shiftNamesSG1 = program.shiftGroupMap.get(ShiftGroupNameOrM1).getShiftList();
            ShiftGroup sg2 = mergeHelper(mergeObject2);
            List<String> shiftNamesSG2 = sg2.getShiftList();
            if (lo.equals(LogicalOperator.AND)) {
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2);
                System.out.println(shiftNamesSG1);
                if (!shiftNamesSG1.isEmpty()) {
                    result = new ArrayList<>(set1);
                    System.out.println(result);
                    System.out.println(shiftNamesSG1 + " Changed?");//Union of results.
                } else {
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {

                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
            } else if (lo.equals(LogicalOperator.XOR)) {
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
            } else if (lo.equals(LogicalOperator.EXCEPT)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2); //set1 is an intersection
                set.removeAll(set1);
                result = new ArrayList<>(set);

            } else {
                //TODO: Maybe throw operator not found exception.
            }
        } else if (isMergeName1 && !isMergeName2 && isSG2) {
            ShiftGroup sg1 = mergeHelper(mergeObject1);
            List<String> shiftNamesSG1 = sg1.getShiftList();
            List<String> shiftNamesSG2 = program.shiftGroupMap.get(ShiftGroupNameOrMergeName).getShiftList();
            if (lo.equals(LogicalOperator.AND)) {
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2);
                System.out.println(shiftNamesSG1);
                if (!shiftNamesSG1.isEmpty()) {
                    result = new ArrayList<>(set1);
                    System.out.println(result);
                    System.out.println(shiftNamesSG1 + " Changed?");//Union of results.
                } else {
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
            } else if (lo.equals(LogicalOperator.XOR)) {
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
            } else if (lo.equals(LogicalOperator.EXCEPT)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2); //set1 is an intersection
                set.removeAll(set1);
                result = new ArrayList<>(set);

            } else {
                //TODO: Maybe throw operator not found exception.
            }
        } else if (isMergeName1 && isMergeName2) {
            ShiftGroup sg1 = mergeHelper(mergeObject1);
            List<String> shiftNamesSG1 = sg1.getShiftList();
            ShiftGroup sg2 = mergeHelper(mergeObject2);
            List<String> shiftNamesSG2 = sg2.getShiftList();
            if (lo.equals(LogicalOperator.AND)) {
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2);
                System.out.println(shiftNamesSG1);
                if (!shiftNamesSG1.isEmpty()) {
                    result = new ArrayList<>(set1);
                    System.out.println(result);
                    System.out.println(shiftNamesSG1 + " Changed?");//Union of results.
                } else {
                    throw new ResultNotFound("No Union Found");
                }
            } else if (lo.equals(LogicalOperator.OR)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                set.addAll(shiftNamesSG2);
                result = new ArrayList<>(set);
            } else if (lo.equals(LogicalOperator.XOR)) {
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
            } else if (lo.equals(LogicalOperator.EXCEPT)) {
                Set<String> set = new HashSet<>();
                set.addAll(shiftNamesSG1);
                Set<String> set1 = new HashSet<>();
                set1.addAll(shiftNamesSG1);
                Set<String> set2 = new HashSet<>();
                set2.addAll(shiftNamesSG2);
                set1.retainAll(set2); //set1 is an intersection
                set.removeAll(set1);
                result = new ArrayList<>(set);

            } else {
                //TODO: Maybe throw operator not found exception.
            }
        } else {
            throw new NameNotFoundException("Shift Group or Merge Name not found");
        }
        return new ShiftGroup(name, result);
    }

    @Override
    public Void visit(Loop l) throws ProgramValidationException {
        validator.validate(l);

        List<String> entities = program.entityGroupMap.get(l.getNameEEG()).getEntities();
//        List<Entity> entities = program.entityMap.entrySet().stream().filter(e -> entityList.contains(e.getKey()))
//                .map(Map.Entry::getValue)
//                .collect(Collectors.toList());

        List<String> shiftList = program.shiftGroupMap.get(l.getNameSSG()).getShiftList();
        System.out.println(shiftList);
        List<Shift> shifts = program.shiftMap.entrySet().stream().filter(e -> shiftList.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        System.out.println(shifts);

        Integer i = 0;
        Integer repeat = 0;

        TimeUnit tU = l.timeUnit;
        Integer num = l.getNum();
        if (l.getVarOrFunc() != null) {
            num = varOrfuncCheckHelper(l.getVarOrFunc());
        }
        System.out.println(num + " this num in evaluator loop");

        while (repeat < l.getRepNum()) {

            for (String e : entities) {

                for (Shift s : shifts) {

                    ScheduledEvent scheduledEvent = getShiftedScheduledEvent(s.getName(), s.getOpen(), s.getClose(),
                            l.getB0(), i, tU);
//                    ScheduledEvent scheduledEvent = new ScheduledEvent(s.getOpen().plusDays(i),
//                                                                        s.getClose().plusDays(i),
//                                                                        s.getName());
                    if (!scheduleMap.containsKey(e)) {
                        scheduleMap.put(e, new HashSet<>());
                    }
                    scheduleMap.get(e).add(scheduledEvent);
                }

                i += num;
                // if (l.getB0() == BitwiseOperator.RIGHTSHIFT) {
                //     i += num;
                // } else {
                //     i -= num;
                // }
            }
            repeat++;
        }

        return null;
    }

    void applyShiftToEntity(Shift shift, String entityName, BitwiseOperator bO, Integer num, TimeUnit tU) {
        ScheduledEvent scheduledEvent;

        LocalDateTime start = shift.getOpen();
        LocalDateTime end = shift.getClose();
        String name = shift.getName();
        if (bO != null) {
            scheduledEvent = getShiftedScheduledEvent(name, start, end, bO, num, tU);
        } else {
            scheduledEvent = new ScheduledEvent(shift.getOpen(), shift.getClose(), shift.getName());
        }

        // todo: change LocalDateTime to Calendar from the get go so this works
        if (!scheduleMap.containsKey(entityName)) {
            scheduleMap.put(entityName, new HashSet<>());
        }
        scheduleMap.get(entityName).add(scheduledEvent);
    }

    private ScheduledEvent getShiftedScheduledEvent(String title, LocalDateTime start, LocalDateTime end,
                                                    BitwiseOperator b0, Integer num, TimeUnit tU) {
        return switch (tU) {
            case HOURS -> b0 == BitwiseOperator.LEFTSHIFT ? new ScheduledEvent(start.minusHours(num),
                    end.minusHours(num), title) : new ScheduledEvent(start.plusHours(num), end.plusHours(num), title);
            case DAYS -> b0 == BitwiseOperator.LEFTSHIFT ? new ScheduledEvent(start.minusDays(num),
                    end.minusDays(num), title) : new ScheduledEvent(start.plusDays(num), end.plusDays(num), title);
            case WEEKS -> b0 == BitwiseOperator.LEFTSHIFT ? new ScheduledEvent(start.minusWeeks(num),
                    end.minusWeeks(num), title) : new ScheduledEvent(start.plusWeeks(num), end.plusWeeks(num), title);
            case MONTHS -> b0 == BitwiseOperator.LEFTSHIFT ? new ScheduledEvent(start.minusMonths(num),
                    end.minusMonths(num), title) : new ScheduledEvent(start.plusMonths(num), end.plusMonths(num),
                    title);
            case YEARS -> b0 == BitwiseOperator.LEFTSHIFT ? new ScheduledEvent(start.minusYears(num),
                    end.minusYears(num), title) : new ScheduledEvent(start.plusYears(num), end.plusYears(num), title);
            default -> throw new RuntimeException("Unrecognized time unit");
        };
    }
}
