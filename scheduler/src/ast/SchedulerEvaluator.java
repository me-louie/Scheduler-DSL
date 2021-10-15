package ast;

import ast.math.Expression;
import ast.math.MathOperation;
import ast.math.Variable;
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
    Program program = Program.getInstance();

    @Override
    public Void visit(Program p) throws ProgramValidationException {
        p.entityMap.values().forEach(e -> e.accept(this));
        p.entityGroupMap.values().forEach(eg -> eg.accept(this));
        p.shiftMap.values().forEach(s -> s.accept(this));
        p.shiftGroupMap.values().forEach(sg -> sg.accept(this));
        p.variableMap.values().forEach(v -> v.accept(this)); // todo: implement these so that we can validate/calculate each function ahead of time, then everything that uses a function can just grab the value
        p.expressionMap.values().forEach(f -> f.accept(this));
        p.transformationMap.values().forEach(tl -> tl.forEach(t -> t.accept(this)));
        return null;
    }

    @Override
    public Void visit(Entity e) throws ProgramValidationException {
        Validator.validate(e);
        // no evaluation
        return null;
    }

    @Override
    public Void visit(EntityGroup eg) throws ProgramValidationException {
        Validator.validate(eg);
        // no evaluation
        return null;
    }

    @Override
    public Void visit(Shift s) throws ProgramValidationException {
        Validator.validate(s);
        // no evaluation
        return null;
    }

    @Override
    public Void visit(ShiftGroup sg) throws ProgramValidationException {
        Validator.validate(sg);
        // no evaluation
        return null;
    }

    @Override
    // todo: add method signatures for the transformations, or maybe this is better put as class descriptions
    public Void visit(Apply a) throws ProgramValidationException {
        Validator.validate(a);

        String entityOrEntityGroupName = a.getNameEEG();
        String shiftOrShiftGroupName = a.getNameSGMG();
        boolean isEntity = program.entityMap.containsKey(entityOrEntityGroupName);
        boolean isShift = program.shiftMap.containsKey(shiftOrShiftGroupName);
        OffsetOperator offsetOperator = a.getOffsetOperator();
        TimeUnit timeUnit = a.getTimeUnit();
        Integer offsetNumber = a.getVarOrFunc() != null ? varOrfuncCheckHelper(a.getVarOrFunc()) :  a.getOffsetAmount();

        if (isEntity && isShift) { // is an entity and a shift
            applyShiftToEntity(program.shiftMap.get(shiftOrShiftGroupName), entityOrEntityGroupName, offsetOperator, offsetNumber, timeUnit);
        } else if (isEntity && !isShift) { // is an entity and a shift group
            for (String shiftName : program.shiftGroupMap.get(shiftOrShiftGroupName).getShifts()) {
                applyShiftToEntity(program.shiftMap.get(shiftName), entityOrEntityGroupName, offsetOperator, offsetNumber, timeUnit);
            }
        } else if (!isEntity && isShift) { // is an entity group and a shift
            Shift shift = program.shiftMap.get(shiftOrShiftGroupName);
            for (String entityName : program.entityGroupMap.get(entityOrEntityGroupName).getEntities()) {
                applyShiftToEntity(shift, entityName, offsetOperator, offsetNumber, timeUnit);
            }
        } else { // is an entity group and a shift group
            for (String entityName : program.entityGroupMap.get(entityOrEntityGroupName).getEntities()) {
                for (String shiftName : program.shiftGroupMap.get(shiftOrShiftGroupName).getShifts()) {
                    applyShiftToEntity(program.shiftMap.get(shiftName), entityName, offsetOperator, offsetNumber, timeUnit);
                }
            }
        }
        return null;
    }

    private void applyShiftToEntity(Shift shift, String entityName, OffsetOperator offsetOperator, Integer offsetAmount, TimeUnit timeUnit) {
        LocalDateTime start = shift.getBegin();
        LocalDateTime end = shift.getEnd();
        String name = shift.getName();
        String description = shift.getDescription();
        ScheduledEvent scheduledEvent = offsetOperator != null ?
                getShiftedScheduledEvent(name, start, end, description, offsetOperator, offsetAmount, timeUnit) :
                new ScheduledEvent(shift.getBegin(), shift.getEnd(), shift.getName(), shift.getDescription());

        if (!scheduleMap.containsKey(entityName)) {
            scheduleMap.put(entityName, new HashSet<>());
        }
        scheduleMap.get(entityName).add(scheduledEvent);
    }

    @Override
    public Void visit(Merge m) throws ProgramValidationException {
        // sometimes we visit nodes repeatedly, in these cases we don't want to re-put a node
        if (!program.shiftGroupMap.containsKey(m.getName())) {
            Validator.validate(m);
            ShiftGroup result = mergeHelper(m);
            program.shiftGroupMap.put(result.getName(), result);
        }
        return null;
    }

    private ShiftGroup mergeHelper(Merge m) {
        List<String> result = null;
        String name = m.getName();
        String shiftGroupOrMergeGroup1Name = m.getShiftGroupOrMergeGroupName1();
        String shiftGroupOrMergeGroup2Name = m.getShiftGroupOrMergeGroupName2();
        SetOperator setOperator = m.getSetOperator();
        List<String> sgomg1ShiftNames = getShiftGroupOrMergeGroupShifts(shiftGroupOrMergeGroup1Name);
        List<String> sgomg2ShiftNames = getShiftGroupOrMergeGroupShifts(shiftGroupOrMergeGroup2Name);

        if (setOperator.equals(SetOperator.AND)) {
            Set<String> resultSet = new HashSet<>(sgomg1ShiftNames);
            resultSet.retainAll(sgomg2ShiftNames);
            if (resultSet.isEmpty()) {
                throw new ResultNotFound("No Union Found");
            }
            result = new ArrayList<>(resultSet);
        } else if (setOperator.equals(SetOperator.OR)) {
            Set<String> resultSet = new HashSet<>(sgomg1ShiftNames);
            resultSet.addAll(sgomg2ShiftNames);
            result = new ArrayList<>(resultSet);
        } else if (setOperator.equals(SetOperator.XOR)) {
            Set<String> resultSet = new HashSet<>(sgomg1ShiftNames);
            resultSet.addAll(sgomg2ShiftNames);
            Set<String> intersectionSet = new HashSet<>(sgomg1ShiftNames); // todo: do these have to be sets?
            intersectionSet.retainAll(new HashSet<>(sgomg2ShiftNames)); // intersection between sgomg1ShiftNames and sgomg2ShiftNames
            resultSet.removeAll(intersectionSet);
            result = new ArrayList<>(resultSet);
        } else if (setOperator.equals(SetOperator.EXCEPT)) {
            Set<String> resultSet = new HashSet<>(sgomg1ShiftNames);
            Set<String> intersectionSet = new HashSet<>(sgomg1ShiftNames);
            intersectionSet.retainAll(new HashSet<>(sgomg2ShiftNames)); // intersection between sgomg1ShiftNames and sgomg2ShiftNames
            resultSet.removeAll(intersectionSet);
            result = new ArrayList<>(resultSet);
            // todo: for this does it work if we just do
            //      Set<String> resultSet = new HashSet<>(sgomg1ShiftNames);
            //      resultSet.removeAll(sgomg2ShiftNames);
        }
        return new ShiftGroup(name, result);
    }

    private List<String> getShiftGroupOrMergeGroupShifts(String shiftGroupOrMergeGroup1Name) {
        // the args of a merge can either be shiftgroups or other merges, determine which this is
        if (!program.shiftGroupMap.containsKey(shiftGroupOrMergeGroup1Name)) {
            Merge merge = (Merge) program.transformationMap.get(Transformation.MERGE)
                    .stream().filter(mrg -> mrg.getName().equals(shiftGroupOrMergeGroup1Name)).findAny().get();
            ShiftGroup mergeResult = mergeHelper(merge);
            return mergeResult.getShifts();
        }
        return program.shiftGroupMap.get(shiftGroupOrMergeGroup1Name).getShifts();
    }

    @Override
    public Void visit(IfThenElse ifThenElse) throws ProgramValidationException {
        Validator.validate(ifThenElse);
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
        SetOperator setOperator = cond.getOperator();
        String shiftGroupOrMergeGroupName1 = cond.getShiftGroupOrMergeGroupName1();
        String shiftGroupOrMergeGroupName2 = cond.getShiftGroupOrMergeGroupName2();

        Set<String> shiftGroup1 = new HashSet<>(program.shiftGroupMap.get(shiftGroupOrMergeGroupName1).getShifts());
        Set<String> shiftGroup2 = new HashSet<>(program.shiftGroupMap.get(shiftGroupOrMergeGroupName2).getShifts());
        Set<String> resultantShifts = new HashSet<>(shiftGroup1);

        if (setOperator == SetOperator.AND) {
            resultantShifts.stream().filter(shiftGroup2::contains).collect(Collectors.toSet());
        } else if (setOperator == SetOperator.OR) {
            resultantShifts.addAll(shiftGroup2);
        } else if (setOperator == SetOperator.XOR) {
            resultantShifts.addAll(shiftGroup2);
            shiftGroup1.retainAll(shiftGroup2);     //SG1 is the intersection
            resultantShifts.removeAll(shiftGroup1);
        }
        cond.setState(!resultantShifts.isEmpty());
        return null;
    }

    @Override
    public Void visit(Expression f) throws ProgramValidationException {
        Validator.validate(f);
        return null;
    }

    @Override
    public Void visit(Variable v) throws ProgramValidationException {
        Validator.validate(v);
        return null;
    }

    @Override
    public Void visit(Loop l) throws ProgramValidationException {
        Validator.validate(l);

        List<String> entities = program.entityGroupMap.get(l.getNameEEG()).getEntities();
        List<String> shiftList = program.shiftGroupMap.get(l.getNameSSG()).getShifts();
        List<Shift> shifts = program.shiftMap.entrySet().stream().filter(e -> shiftList.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        TimeUnit timeUnit = l.timeUnit;
        Integer offsetAmount = l.getVarOrFunc() == null ? l.getOffsetAmount() : varOrfuncCheckHelper(l.getVarOrFunc());

        for (int i = 0; i < l.getRepeatAmount(); i++) {
            for (String e : entities) {
                for (Shift s : shifts) {
                    ScheduledEvent scheduledEvent = getShiftedScheduledEvent(s.getName(), s.getBegin(), s.getEnd(),
                            s.getDescription(), l.getOffsetOperator(), i * offsetAmount, timeUnit);
                    if (!scheduleMap.containsKey(e)) {
                        scheduleMap.put(e, new HashSet<>());
                    }
                    scheduleMap.get(e).add(scheduledEvent);
                }
            }
        }
        return null;
    }

    private Integer varVal(Variable var) {
        Integer value;
        if (var.getAlias() != null) {
            if (program.variableMap.containsKey(var.getAlias())) {
                value = varVal(program.variableMap.get(var.getAlias()));
            } else {
                throw new NameNotFoundException(var.getAlias() + " var name not present");
            }
        } else {
            value = var.getValue();
        }
        return value;
    }

    private Integer getMathVal(Integer num1, Integer num2, MathOperation mathOP) {
        return switch (mathOP) {
            case PLUS -> num1 + num2;
            case MINUS -> num1 - num2;
            case MULTIPLY -> num1 * num2;
            case DIVIDE -> num1 / num2;
            case POWER -> (int) Math.pow(num1, num2);
        };
    }

    private Integer varOrfuncCheckHelper(String name) {
        Integer result;
        System.out.println(name);
        if (program.variableMap.containsKey(name)) {
            result = varVal(program.variableMap.get(name));
        } else if (program.expressionMap.containsKey(name)) {
            result = funcVal(program.expressionMap.get(name));
        } else {
            throw new NameNotFoundException(name + " VAR OR FUNCTION NAME not present");
        }
        return result;
    }

    private Integer funcVal(Expression func) {
        Integer value = null, num1, num2;
        MathOperation mathOP = func.mathOperation;
        if (func.getValue1() != null && func.getValue2() != null) {
            num1 = func.getValue1();
            num2 = func.getValue2();
            value = getMathVal(num1, num2, mathOP);
            System.out.println(value);
        } else if (func.getValue1() == null && func.getVariableOrExpressionName1() != null && func.getValue2() != null) {
            num1 = varOrfuncCheckHelper(func.getVariableOrExpressionName1());
            num2 = func.getValue2();
            value = getMathVal(num1, num2, mathOP);
            System.out.println(value);
        } else if (func.getValue1() != null && func.getValue2() == null && func.getVariableOrExpressionName2() != null) {
            num2 = varOrfuncCheckHelper(func.getVariableOrExpressionName2());
            num1 = func.getValue1();
            value = getMathVal(num1, num2, mathOP);
            System.out.println(value);
        } else if (func.getValue1() == null && func.getVariableOrExpressionName1() != null && func.getValue2() == null && func.getVariableOrExpressionName2() != null) {
            num1 = varOrfuncCheckHelper(func.getVariableOrExpressionName1());
            num2 = varOrfuncCheckHelper(func.getVariableOrExpressionName2());
            value = getMathVal(num1, num2, mathOP);
            System.out.println(value);
        }
        return value;
    }

    private ScheduledEvent getShiftedScheduledEvent(String title, LocalDateTime begin, LocalDateTime end, String description,
                                                    OffsetOperator offsetOperator, Integer offsetAmount, TimeUnit timeUnit) {
        offsetAmount = offsetOperator == OffsetOperator.LEFTSHIFT ? offsetAmount * -1 : offsetAmount;
        return switch (timeUnit) {
            case HOURS -> new ScheduledEvent(begin.plusHours(offsetAmount), end.plusHours(offsetAmount), title, description);
            case DAYS -> new ScheduledEvent(begin.plusDays(offsetAmount), end.plusDays(offsetAmount), title, description);
            case WEEKS -> new ScheduledEvent(begin.plusWeeks(offsetAmount), end.plusWeeks(offsetAmount), title, description);
            case MONTHS -> new ScheduledEvent(begin.plusMonths(offsetAmount), end.plusMonths(offsetAmount), title, description);
            case YEARS -> new ScheduledEvent(begin.plusYears(offsetAmount), end.plusYears(offsetAmount), title, description);
        };
    }
}
