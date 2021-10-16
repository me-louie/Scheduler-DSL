package validate;

import ast.*;
import ast.math.Expression;
import ast.math.Variable;
import ast.transformation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Validator {

    private static final Program program = Program.getInstance();

    public static void validate(Entity entity) throws ProgramValidationException {
        String entityName = entity.getName();
        if (!isUniqueEntityEntityGroup(entityName)) {
            throw new DuplicateNameException("2 or more entity/entity groups share the name " + entityName);
        }
    }

    public static void validate(EntityGroup entityGroup) throws ProgramValidationException {
        String entityGroupName = entityGroup.getName();
        if (!isUniqueEntityEntityGroup(entityGroupName)) {
            throw new DuplicateNameException("2 or more entity/entity groups share the name " + entityGroupName);
        }
    }

    public static void validate(Shift shift) throws ProgramValidationException {
        String shiftName = shift.getName();
        LocalDateTime start = shift.getBegin();
        LocalDateTime end = shift.getEnd();
        if (!isUniqueShiftShiftGroupMergeName(shiftName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftName);
        } else if (!isValidDateTimeRange(start, end)) {
            throw new InvalidDateTimeRangeException(start + " to " + end + " is not a valid date range.");
        }
    }

    public static void validate(ShiftGroup shiftGroup) throws ProgramValidationException {
        String shiftGroupName = shiftGroup.getName();
        List<String> shiftNames = shiftGroup.getShifts();
        if (!isUniqueShiftShiftGroupMergeName(shiftGroupName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftGroupName);
        }
        for (String shiftName : shiftNames) {
            if (!shiftExists(shiftName)) {
                throw new NameNotFoundException("There is no shift named " + shiftName);
            } else if (!isUniqueShiftShiftGroupMergeName(shiftName)) {
                throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftName);
            }
        }
    }

    public static void validate(Apply apply) throws ProgramValidationException {
        String shiftOrShiftGroupName = apply.getShiftOrShiftGroupOrMergeGroupName();
        String entityOrEntityGroupName = apply.getEntityOrEntityGroupName();
        if (!shiftExists(shiftOrShiftGroupName) && !shiftGroupExists(shiftOrShiftGroupName)) {
            throw new NameNotFoundException("There is no shift/shift group named " + shiftOrShiftGroupName);
        } else if (!isUniqueShiftShiftGroupMergeName(shiftOrShiftGroupName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftOrShiftGroupName);
        } else if (!entityExists(entityOrEntityGroupName) && !entityGroupExists(entityOrEntityGroupName)) {
            throw new NameNotFoundException("There is no entity/entity group named " + entityOrEntityGroupName);
        } else if (!isUniqueEntityEntityGroup(entityOrEntityGroupName)) {
            throw new DuplicateNameException("2 or more entity/entity groups share the name " + entityOrEntityGroupName);
        } else if (apply.getTimeUnit() != null && !isValueOrVariableOrExpression(apply.getOffsetAmount(), apply.getVarOrExpression())) {
            throw new NameNotFoundException("There is no variable/expression named " + apply.getVarOrExpression());
        }
    }

    public static void validate(Merge merge) throws ProgramValidationException {
        String mergeGroupName = merge.getName();
        String shiftGroup1Name = merge.getShiftGroupOrMergeGroupName1();
        String shiftGroup2Name = merge.getShiftGroupOrMergeGroupName2();
        if (!isUniqueShiftShiftGroupMergeName(mergeGroupName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + mergeGroupName);
        } else if (!shiftGroupExists(shiftGroup1Name) && !mergeGroupExists(shiftGroup1Name)) {
            throw new NameNotFoundException("There is no merge/shift group named " + shiftGroup1Name);
        } else if (!shiftGroupExists(shiftGroup2Name) && !mergeGroupExists(shiftGroup2Name)) {
            throw new NameNotFoundException("There is no merge/shift group named " + shiftGroup2Name);
        } else if (!isUniqueShiftShiftGroupMergeName(shiftGroup1Name)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftGroup1Name);
        } else if (!isUniqueShiftShiftGroupMergeName(shiftGroup2Name)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftGroup2Name);
        }
    }

    public static void validate(Loop loop) throws ProgramValidationException {
        String shiftGroupName = loop.getShiftOrShiftGroupOrMergeGroupName();
        String entityGroupName = loop.getEntityOrEntityGroupName();
        if (!shiftGroupExists(shiftGroupName)) {
            throw new NameNotFoundException("There is no shift group named " + shiftGroupName);
        } else if (!isUniqueShiftShiftGroupMergeName(shiftGroupName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftGroupName);
        } else if (!entityGroupExists(entityGroupName)) {
            throw new NameNotFoundException("There is no entity group named " + entityGroupName);
        } else if (!isUniqueEntityEntityGroup(entityGroupName)) {
            throw new DuplicateNameException("2 or more entity/entity groups share the name " + entityGroupName);
        } else if (loop.getOffsetAmount() == null && loop.getVarOrExpression() == null) {
            throw new ValueNotProvidedException("A loop was provided without a valid offset amount (either a constant, variable, or expression)");
        } else if (!isValueOrVariableOrExpression(null, loop.getVarOrExpression())) {
            throw new NameNotFoundException("There is no variable/expression named " + loop.getVarOrExpression());
        }
    }

    public static void validate(IfThenElse ifThenElse) throws ProgramValidationException {
        validate(ifThenElse.getCond());
    }

    private static void validate(Cond cond) throws ProgramValidationException {
        String shiftGroupOrMergeGroupName1 = cond.getShiftGroupOrMergeGroupName1();
        String shiftGroupOrMergeGroupName2 = cond.getShiftGroupOrMergeGroupName2();
        if (!shiftGroupExists(shiftGroupOrMergeGroupName1)) {
            throw new NameNotFoundException("There is no shift/merge group named " + shiftGroupOrMergeGroupName1);
        } else if (!shiftGroupExists(shiftGroupOrMergeGroupName2)) {
            throw new NameNotFoundException("There is no shift/merge group named " + shiftGroupOrMergeGroupName2);
        }
    }

    public static void validate(Expression expression) {
        String variableOrExpressionName1 = expression.getVariableOrExpressionName1();
        String variableOrExpressionName2 = expression.getVariableOrExpressionName2();
        if (variableOrExpressionName1 == null && expression.getValue1() == null) {
            throw new ValueNotProvidedException("Expression " + expression.getName() + " has neither a constant or expression/variable for a first argument.");
        } else if (variableOrExpressionName2 == null && expression.getValue2() == null) {
            throw new ValueNotProvidedException("Expression " + expression.getName() + " has neither a constant or expression/variable for a second argument.");
        } else if (!isValueOrVariableOrExpression(expression.getValue1(), variableOrExpressionName1)) {
            throw new NameNotFoundException(variableOrExpressionName1 + " is not defined as either a variable or expression");
        } else if (!isValueOrVariableOrExpression(expression.getValue2(), variableOrExpressionName2)) {
            throw new NameNotFoundException(variableOrExpressionName2 + " is not defined as either a variable or expression");
        }
    }

    private static boolean isValueOrVariableOrExpression(Integer value, String variableOrExpressionName) {
        return value != null ||
                !program.expressionMap.containsKey(variableOrExpressionName) ||
                !program.variableMap.containsKey(variableOrExpressionName);
    }

    public static void validate(Variable variable) {
        String aliasName = variable.getAlias();
        if (variable.getValue() == null && aliasName == null) {
            throw new ValueNotProvidedException("Variable " + variable.getName() + " has neither a constant or expression/variable as argument.");
        } else if (!isValueOrVariableOrExpression(variable.getValue(), aliasName)) {
            throw new NameNotFoundException(aliasName + " is not defined as either a variable or expression");
        }
    }

    private static boolean isValidDateTimeRange(LocalDateTime begin, LocalDateTime end) {
        // we allow times/dates from the past
        return begin.isBefore(end) || begin.isEqual(end);
    }

    private static boolean isUniqueShiftShiftGroupMergeName(String name) {
        Collection<Shift> shifts = program.shiftMap.values();
        // We use shiftGroupsWithoutMergeGroups because the result of a merge gets added to the list of shiftGroups.
        // This way a merge that has already been executed won't be double-counted as a merge and a shift group.
        List<ShiftGroup> shiftGroups = program.shiftGroupsWithoutMergeGroups;
        List<Transformation> merges = program.transformationMap.get(Transformation.MERGE);
        List<Transformation> nestedThenMerges = new ArrayList<>();
        List<Transformation> nestedElseMerges = new ArrayList<>();
        program.transformationMap.get(Transformation.IF_THEN_ELSE).forEach(ifelse -> {
            ((IfThenElse) ifelse).getThenTransformations().forEach(transformation -> {
                if (transformation.getClass().equals(Merge.class)) {
                    nestedThenMerges.add(transformation);
                }
            });
            ((IfThenElse) ifelse).getElseTransformations().forEach(transformation -> {
                if (transformation.getClass().equals(Merge.class)) {
                    nestedElseMerges.add(transformation);
                }
            });
        });

        long shiftCount = shifts.stream().filter(shift -> shift.getName().equals(name)).count();
        long shiftGroupCount = shiftGroups.stream().filter(shiftGroup -> shiftGroup.getName().equals(name)).count();
        long mergeCount = merges.stream().filter(merge -> merge.getName().equals(name)).count();
        long nestedThenMergeCount = nestedThenMerges.stream().filter(merge -> merge.getName().equals(name)).count();
        long nestedElseMergeCount = nestedElseMerges.stream().filter(merge -> merge.getName().equals(name)).count();

        return shiftCount + shiftGroupCount + mergeCount + nestedThenMergeCount == 1 ||
                shiftCount + shiftGroupCount + mergeCount + nestedElseMergeCount == 1;
    }

    private static boolean isUniqueEntityEntityGroup(String name) {
        Collection<Entity> entities = program.entityMap.values();
        Collection<EntityGroup> entityGroups = program.entityGroupMap.values();

        long entityCount = entities.stream().filter(entity -> entity.getName().equals(name)).count();
        long entityGroupCount = entityGroups.stream().filter(entityGroup -> entityGroup.getName().equals(name)).count();

        return entityCount + entityGroupCount == 1;
    }

    private static boolean entityExists(String name) {
        return program.entityMap.containsKey(name);
    }

    private static boolean entityGroupExists(String name) {
        return program.entityGroupMap.containsKey(name);
    }

    private static boolean shiftExists(String name) {
        return program.shiftMap.containsKey(name);
    }

    private static boolean shiftGroupExists(String name) {
        return program.shiftGroupMap.containsKey(name);
    }

    private static boolean mergeGroupExists(String name) {
        return program.transformationMap.get(Transformation.MERGE)
                .stream().anyMatch(merge -> merge.getName().equals(name));
    }
}
