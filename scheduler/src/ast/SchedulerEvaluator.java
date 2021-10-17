package ast;

import ast.math.Expression;
import ast.math.MathOperation;
import ast.math.Variable;
import ast.transformation.*;
import evaluate.ScheduledEvent;
import validate.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ast.transformation.SetOperator.*;

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
        p.variableMap.values().forEach(v -> v.accept(this));
        p.expressionMap.values().forEach(f -> f.accept(this));
        // hack to make this work: since transformationMap makes no guarantees about the order transformations will
        // be visited in
        // we need to have merges done ahead of time to avoid NameNotFoundExceptions
        p.transformationMap.get(Transformation.MERGE).forEach(m -> m.accept(this));
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
    public Void visit(Apply a) throws ProgramValidationException {
        Validator.validate(a);

        String entityOrEntityGroupName = a.getEntityOrEntityGroupName();
        String shiftOrShiftGroupName = a.getShiftOrShiftGroupOrMergeGroupName();
        boolean isEntity = program.entityMap.containsKey(entityOrEntityGroupName);
        boolean isShift = program.shiftMap.containsKey(shiftOrShiftGroupName);
        OffsetOperator offsetOperator = a.getOffsetOperator();
        TimeUnit timeUnit = a.getTimeUnit();
        Integer offsetAmount = a.getVarOrExpression() != null ? getVarOrExpressionFinalValue(a.getVarOrExpression())
                : a.getOffsetAmount();

        for (int i = 0; i < a.getRepeatAmount(); i++) {
            if (isEntity && isShift) { // is an entity and a shift
                applyShiftToEntity(program.shiftMap.get(shiftOrShiftGroupName), entityOrEntityGroupName, offsetOperator,
                        i * offsetAmount, timeUnit);
            } else if (isEntity && !isShift) { // is an entity and a shift group
                for (String shiftName : program.shiftGroupMap.get(shiftOrShiftGroupName).getShifts()) {
                    applyShiftToEntity(program.shiftMap.get(shiftName), entityOrEntityGroupName, offsetOperator,
                            i * offsetAmount, timeUnit);
                }
            } else if (!isEntity && isShift) { // is an entity group and a shift
                Shift shift = program.shiftMap.get(shiftOrShiftGroupName);
                for (String entityName : program.entityGroupMap.get(entityOrEntityGroupName).getEntities()) {
                    applyShiftToEntity(shift, entityName, offsetOperator, i * offsetAmount, timeUnit);
                }
            } else { // is an entity group and a shift group
                for (String entityName : program.entityGroupMap.get(entityOrEntityGroupName).getEntities()) {
                    for (String shiftName : program.shiftGroupMap.get(shiftOrShiftGroupName).getShifts()) {
                        applyShiftToEntity(program.shiftMap.get(shiftName), entityName, offsetOperator,
                                i * offsetAmount,
                                timeUnit);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Void visit(Merge m) throws ProgramValidationException {
        // Check if this node has already been visited. Since merge statements can take as argument merges that haven't
        // happened yet it's possible we visit a node multiple times. In these case skip validation/evaluation
        if (!program.shiftGroupMap.containsKey(m.getName())) {
            Validator.validate(m);
            ShiftGroup result = mergeHelper(m);
            program.shiftGroupMap.put(result.getName(), result);
        }
        return null;
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
        SetOperator setOperator = cond.getSetOperator();
        String shiftGroupOrMergeGroupName1 = cond.getShiftGroupOrMergeGroupName1();
        String shiftGroupOrMergeGroupName2 = cond.getShiftGroupOrMergeGroupName2();

        Set<String> shiftGroup1 = new HashSet<>(program.shiftGroupMap.get(shiftGroupOrMergeGroupName1).getShifts());
        Set<String> shiftGroup2 = new HashSet<>(program.shiftGroupMap.get(shiftGroupOrMergeGroupName2).getShifts());
        Set<String> resultantShifts = new HashSet<>(shiftGroup1);

        if (setOperator == AND) {
            resultantShifts = resultantShifts.stream().filter(shiftGroup2::contains).collect(Collectors.toSet());
        } else if (setOperator == OR) {
            resultantShifts.addAll(shiftGroup2);
        } else if (setOperator == XOR) {
            resultantShifts.addAll(shiftGroup2);
            shiftGroup1.retainAll(shiftGroup2);     // shiftGroup1 is now the intersection of shiftGroup1 and
            // shiftGroup2
            resultantShifts.removeAll(shiftGroup1);
        } else if (setOperator == EXCEPT) {
            resultantShifts.removeAll(shiftGroup2);
        }
        cond.setState(!resultantShifts.isEmpty());
        return null;
    }

    @Override
    public Void visit(Expression e) throws ProgramValidationException {
        // check if expression has already been visited
        if (e.getFinalValue() != null) {
            return null;
        }
        Validator.validate(e);

        Integer num1 = getExpressionValue(e, true);
        Integer num2 = getExpressionValue(e, false);
        MathOperation mathOperation = e.mathOperation;
        if (num2 == 0 && mathOperation == MathOperation.DIVIDE){
            throw new DivideByZero(e.getName() +" Can't divide by zero");
        }
        Integer result = null;
        try{
            result = calculateExpressionFinalValue(num1, num2, mathOperation);
        } catch (Exception x){
            System.out.println("Arithmetic error associated with : " + e.getName());
           throw x;
        }
        e.setFinalValue(result);
        return null;
    }

    @Override
    public Void visit(Variable v) throws ProgramValidationException {
        // check if variable has already been visited
        if (v.getFinalValue() != null) {
            return null;
        }
        Validator.validate(v);

        Integer finalValue;
        String aliasName = v.getAlias();
        if (program.variableMap.containsKey(aliasName)) { // alias is a variable
            Variable alias = program.variableMap.get(aliasName);
            visit(alias);
            finalValue = alias.getFinalValue();
        } else if (program.expressionMap.containsKey(aliasName)) { // alias is an expression
            Expression alias = program.expressionMap.get(aliasName);
            visit(alias);
            finalValue = alias.getFinalValue();
        } else { // variable doesn't have an alias, is a constant
            finalValue = v.getValue();
        }
        v.setFinalValue(finalValue);
        return null;
    }

    @Override
    public Void visit(Loop l) throws ProgramValidationException {
        Validator.validate(l);

        List<Shift> shifts;
        List<String> shiftList;
        List<String> entities = program.entityGroupMap.get(l.getEntityGroupName()).getEntities();
        boolean isShift = program.shiftMap.containsKey(l.getShiftOrShiftGroupOrMergeGroupName());

        if (isShift) {
            shifts = List.of(program.shiftMap.get(l.getShiftOrShiftGroupOrMergeGroupName()));
        } else {
            shiftList = program.shiftGroupMap.get(l.getShiftOrShiftGroupOrMergeGroupName()).getShifts();
            shifts = program.shiftMap.entrySet().stream().filter(e -> shiftList.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
        TimeUnit timeUnit = l.timeUnit;
        Integer offsetAmount = l.getVarOrExpression() == null ? l.getOffsetAmount() :
                getVarOrExpressionFinalValue(l.getVarOrExpression());

        List<String> entitiesForRepeatedScheduling = new ArrayList<>();
        // Create a new list of entities to make repeated scheduling by offset easier
        for (int i = 0; i < l.getRepeatAmount(); i++) {
            entitiesForRepeatedScheduling.addAll(entities);
        }

        for (int i = 0; i < entitiesForRepeatedScheduling.size(); i++) {
            for (Shift s : shifts) {
                ScheduledEvent scheduledEvent = getShiftedScheduledEvent(s.getName(), s.getBegin(), s.getEnd(),
                        s.getDescription(), l.getOffsetOperator(), i * offsetAmount, timeUnit);
                if (!scheduleMap.containsKey(entitiesForRepeatedScheduling.get(i))) {
                    scheduleMap.put(entitiesForRepeatedScheduling.get(i), new HashSet<>());
                }
                scheduleMap.get(entitiesForRepeatedScheduling.get(i)).add(scheduledEvent);
            }
        }
        return null;
    }

    private ShiftGroup mergeHelper(Merge m) {
        String shiftGroupOrMergeGroupName1 = m.getShiftGroupOrMergeGroupName1();
        String shiftGroupOrMergeGroupName2 = m.getShiftGroupOrMergeGroupName2();
        List<String> sgomg1ShiftNames = getShiftGroupOrMergeGroupShifts(shiftGroupOrMergeGroupName1);
        List<String> sgomg2ShiftNames = getShiftGroupOrMergeGroupShifts(shiftGroupOrMergeGroupName2);
        SetOperator setOperator = m.getSetOperator();

        Set<String> resultSet = new HashSet<>(sgomg1ShiftNames);
        switch (setOperator) {
            case AND -> resultSet.retainAll(sgomg2ShiftNames);
            case OR -> resultSet.addAll(sgomg2ShiftNames);
            case XOR -> {
                Set<String> intersectionSet = new HashSet<>(sgomg1ShiftNames);
                intersectionSet.retainAll(sgomg2ShiftNames); // intersection between sgomg1ShiftNames and
                // some2ShiftNames
                resultSet.addAll(sgomg2ShiftNames);
                resultSet.removeAll(intersectionSet);
            }
            case EXCEPT -> resultSet.removeAll(sgomg2ShiftNames);
        }
        if (resultSet.isEmpty()) {
            throw new ResultNotFoundException("Merging " + shiftGroupOrMergeGroupName1 + " " + setOperator + " " + shiftGroupOrMergeGroupName2 + " resulted in an empty set.");
        }
        return new ShiftGroup(m.getName(), new ArrayList<>(resultSet));
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

    private void applyShiftToEntity(Shift shift, String entityName, OffsetOperator offsetOperator,
                                    Integer offsetAmount, TimeUnit timeUnit) {
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

    private ScheduledEvent getShiftedScheduledEvent(String title, LocalDateTime begin, LocalDateTime end,
                                                    String description,
                                                    OffsetOperator offsetOperator, Integer offsetAmount,
                                                    TimeUnit timeUnit) {
        offsetAmount = offsetOperator == OffsetOperator.LEFTSHIFT ? offsetAmount * -1 : offsetAmount;
        return switch (timeUnit) {
            case HOURS -> new ScheduledEvent(begin.plusHours(offsetAmount), end.plusHours(offsetAmount), title,
                    description);
            case DAYS -> new ScheduledEvent(begin.plusDays(offsetAmount), end.plusDays(offsetAmount), title,
                    description);
            case WEEKS -> new ScheduledEvent(begin.plusWeeks(offsetAmount), end.plusWeeks(offsetAmount), title,
                    description);
            case MONTHS -> new ScheduledEvent(begin.plusMonths(offsetAmount), end.plusMonths(offsetAmount), title,
                    description);
            case YEARS -> new ScheduledEvent(begin.plusYears(offsetAmount), end.plusYears(offsetAmount), title,
                    description);
        };
    }

    // todo: this feels like it belongs in the Loop class, although to make it work
    //       will probably be more work than we bargained for
    private Integer getVarOrExpressionFinalValue(String name) {
        if (program.variableMap.containsKey(name)) {
            Variable var = program.variableMap.get(name);
            return var.getFinalValue();
        } else {
            Expression expression = program.expressionMap.get(name);
            return expression.getFinalValue();
        }
    }

    // todo: this feels like it belongs in the Expression class, same for the method below
    // determines whether the arg to an expression is a number, variable, or another expression and returns its value
    private Integer getExpressionValue(Expression e, boolean isFirstArg) {
        String variableOrExpressionName = isFirstArg ? e.getVariableOrExpressionName1() :
                e.getVariableOrExpressionName2();
        if (program.variableMap.containsKey(variableOrExpressionName)) { // arg to e is a variable
            Variable variable = program.variableMap.get(variableOrExpressionName);
            visit(variable);
            return variable.getFinalValue();
        } else if (program.expressionMap.containsKey(variableOrExpressionName)) { // arg to e is an expression
            Expression expression = program.expressionMap.get(variableOrExpressionName);
            visit(expression);
            return expression.getFinalValue();
        } else { // arg to e is a number
            return isFirstArg ? e.getValue1() : e.getValue2();
        }
    }

    private Integer calculateExpressionFinalValue(Integer num1, Integer num2, MathOperation mathOP) {
        return switch (mathOP) {
            case PLUS -> Math.addExact(num1 , num2);
            case MINUS -> Math.subtractExact(num1, num2);
            case MULTIPLY -> Math.multiplyExact(num1, num2);
            case DIVIDE -> num1 / num2;
            case POWER -> (int) Math.pow(num1, num2);
        };
    }
}
