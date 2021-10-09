package validate;

import ast.*;
import ast.transformation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Validator {

    private final Program program;

    public Validator(Program program) {
        this.program = program;
    }

    public void validate(Entity entity) throws ProgramValidationException {
        String entityName = entity.getName();
        if (!isUniqueEntityEntityGroup(entityName)) {
            throw new DuplicateNameException("2 or more entity/entity groups share the name " + entityName);
        }
    }

    public void validate(EntityGroup entityGroup) throws ProgramValidationException {
        String entityGroupName = entityGroup.getName();
        if (!isUniqueEntityEntityGroup(entityGroupName)) {
            throw new DuplicateNameException("2 or more entity/entity groups share the name " + entityGroupName);
        }
    }

    public void validate(Shift shift) throws ProgramValidationException {
        String shiftName = shift.getName();
        LocalDateTime start = shift.getOpen();
        LocalDateTime end = shift.getClose();
        if (!isUniqueShiftShiftGroupName(shiftName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftName);
        } else if (!isValidDateTimeRange(start, end)) {
            throw new InvalidDateTimeRangeException(start + " to " + end + " is not a valid date range.");
        }
    }

    public void validate(ShiftGroup shiftGroup) throws ProgramValidationException {
        String shiftGroupName = shiftGroup.getName();
        List<String> shiftNames = shiftGroup.getShiftList();
        if (!isUniqueShiftShiftGroupName(shiftGroupName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftGroupName);
        }
        for (String shiftName : shiftNames) {
            if (!shiftExists(shiftName)) {
                throw new NameNotFoundException("There is no shift named " + shiftName);
            } else if (!isUniqueShiftShiftGroupName(shiftName)) {
                throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftName);
            }
        }
    }

    public void validate(Apply apply) throws ProgramValidationException {
        String shiftOrShiftGroupName = apply.getNameSGMG();
        String entityOrEntityGroupName = apply.getNameEEG();
        if (!shiftExists(shiftOrShiftGroupName) && !shiftGroupExists(shiftOrShiftGroupName)) {
            throw new NameNotFoundException("There is no shift/shift group named " + shiftOrShiftGroupName);
        } else if (!isUniqueShiftShiftGroupName(shiftOrShiftGroupName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftOrShiftGroupName);
        } else if (!entityExists(entityOrEntityGroupName) && !entityGroupExists(entityOrEntityGroupName)) {
            throw new NameNotFoundException("There is no entity/entity group named " + entityOrEntityGroupName);
        } else if (!isUniqueEntityEntityGroup(entityOrEntityGroupName)) {
            throw new DuplicateNameException("2 or more entity/entity groups share the name " + entityOrEntityGroupName);
        }
    }

    public void validate(Merge merge) throws ProgramValidationException {
        String mergeGroupName = merge.getName();
        String shiftGroup1Name = merge.getNameSGS1();
        String shiftGroup2Name = merge.getNameSGS2();
        if (shiftExists(mergeGroupName) || shiftGroupExists(mergeGroupName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + mergeGroupName);
        } else if (!isUniqueShiftShiftGroupName(shiftGroup1Name)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftGroup1Name);
        } else if (!isUniqueShiftShiftGroupName(shiftGroup2Name)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftGroup2Name);
        }
    }

    public void validate(Loop loop) throws ProgramValidationException {
        String shiftGroupName = loop.getNameSSG();
        String entityGroupName = loop.getNameEEG();
        if (!shiftGroupExists(shiftGroupName)) {
            throw new NameNotFoundException("There is no shift group named " + shiftGroupName);
        } else if (!isUniqueShiftShiftGroupName(shiftGroupName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftGroupName);
        } else if (!entityGroupExists(entityGroupName)) {
            throw new NameNotFoundException("There is no entity group named " + entityGroupName);
        } else if (!isUniqueEntityEntityGroup(entityGroupName)) {
            throw new DuplicateNameException("2 or more entity/entity groups share the name " + entityGroupName);
        }
    }

    private boolean isValidDateTimeRange(LocalDateTime begin, LocalDateTime end) {
        // currently we allow times/dates from the past
        return begin.isBefore(end) || begin.isEqual(end);
    }

    private boolean isUniqueShiftShiftGroupName(String name) {
        List<Shift> shifts = program.getShifts();
        List<ShiftGroup> shiftGroups = program.getShiftGroups();
        List<Transformation> transformations = program.getTransformations();
        List<Merge> merges = new ArrayList<>();
        transformations = transformations.stream().filter(transformation -> (transformation instanceof Merge)).collect(Collectors.toList());
        transformations.forEach(transformation -> merges.add((Merge) transformation));

        long shiftCount = shifts.stream().filter(shift -> shift.getName().equals(name)).count();
        long shiftGroupCount = shiftGroups.stream().filter(shiftGroup -> shiftGroup.getName().equals(name)).count();
        long mergeCount = merges.stream().filter(merge -> merge.getName().equals(name)).count();

        return shiftCount + shiftGroupCount + mergeCount == 1;
    }

    private boolean isUniqueEntityEntityGroup(String name) {
        List<Entity> entities = program.getEntities();
        List<EntityGroup> entityGroups = program.getEntityGroups();

        long entityCount = entities.stream().filter(entity -> entity.getName().equals(name)).count();
        long entityGroupCount = entityGroups.stream().filter(entityGroup -> entityGroup.getName().equals(name)).count();

        return entityCount + entityGroupCount == 1;
    }

    private boolean entityExists(String name) {
        return program.entityMap.containsKey(name);
    }

    private boolean entityGroupExists(String name) {
        return program.entityGroupMap.containsKey(name);
    }

    private boolean shiftExists(String name) {
        return program.shiftMap.containsKey(name);
    }

    private boolean shiftGroupExists(String name) {
        return program.shiftGroupMap.containsKey(name);
    }
}
