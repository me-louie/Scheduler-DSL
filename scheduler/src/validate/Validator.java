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
            throw new DuplicateNameException("Error: 2 or more entity/entity groups share the name " + entityName);
        }
    }

    public void validate(EntityGroup entityGroup) throws ProgramValidationException {
        String entityGroupName = entityGroup.getName();
        if (!isUniqueEntityEntityGroup(entityGroupName)) {
            throw new DuplicateNameException("Error: 2 or more entity/entity groups share the name " + entityGroupName);
        }
    }

    public void validate(Shift shift) throws ProgramValidationException {
        String shiftName = shift.getName();
        LocalDateTime start = shift.getOpen();
        LocalDateTime end = shift.getClose();
        if (!isUniqueShiftShiftGroupMergeName(shiftName)) {
            throw new DuplicateNameException("Error: 2 or more shift/shift group/merge groups share the name " + shiftName);
        } else if (!isValidDateTimeRange(start, end)) {
            throw new InvalidDateTimeRangeException("Error: " + start + " to " + end + " is not a valid date range.");
        }
    }

    public void validate(ShiftGroup shiftGroup) throws ProgramValidationException {
        String shiftGroupName = shiftGroup.getName();
        List<String> shiftNames = shiftGroup.getShiftList();
        if (!isUniqueShiftShiftGroupMergeName(shiftGroupName)) {
            throw new DuplicateNameException("Error: 2 or more shift/shift group/merge groups share the name " + shiftGroupName);
        }
        for (String shiftName : shiftNames) {
            if (!isUniqueShiftShiftGroupMergeName(shiftName)) {
                throw new DuplicateNameException("Error: 2 or more shift/shift group/merge groups share the name " + shiftName);
            }
        }
    }

    public void validate(Apply apply) throws ProgramValidationException {
        String shiftName = apply.getNameSGMG();
        String entityName = apply.getNameEEG();
        if (!isUniqueShiftShiftGroupMergeName(shiftName)) {
            throw new DuplicateNameException("Error: 2 or more shift/shift group/merge groups share the name " + shiftName);
        } else if (!isUniqueEntityEntityGroup(entityName)) {
            throw new DuplicateNameException("Error: 2 or more entity/entity groups share the name " + entityName);
        }
    }

    public void validate(Merge merge) throws ProgramValidationException {
        String mergeGroupName = merge.getName();
        String shiftGroup1Name = merge.getNameSGS1();
        String shiftGroup2Name = merge.getNameSGS2();
        if (!isUniqueShiftShiftGroupMergeName(mergeGroupName)) {
            throw new DuplicateNameException("Error: 2 or more shift/shift group/merge groups share the name " + mergeGroupName);
        } else if (!isUniqueShiftShiftGroupMergeName(shiftGroup1Name)) {
            throw new DuplicateNameException("Error: 2 or more shift/shift group/merge groups share the name " + shiftGroup1Name);
        } else if (shiftGroup2Name != null && !isUniqueShiftShiftGroupMergeName(shiftGroup2Name)) {
            throw new DuplicateNameException("Error: 2 or more shift/shift group/merge groups share the name " + shiftGroup2Name);
        }
    }

    public void validate(Loop loop) throws ProgramValidationException {
        String shiftGroupName = loop.getNameSSG();
        String entityGroupName = loop.getNameEEG();
        if (!isUniqueShiftShiftGroupMergeName(shiftGroupName)) {
            throw new DuplicateNameException("Error: 2 or more shift/shift group/merge groups share the name " + shiftGroupName);
        } else if (!isUniqueEntityEntityGroup(entityGroupName)) {
            throw new DuplicateNameException("Error: 2 or more entity/entity groups share the name " + entityGroupName);
        }
    }

    boolean isValidDateTimeRange(LocalDateTime begin, LocalDateTime end) {
        // currently we allow times/dates from the past
        return begin.isBefore(end) || begin.isEqual(end);
    }

    boolean isUniqueShiftShiftGroupMergeName(String name) {
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

    boolean isUniqueEntityEntityGroup(String name) {
        List<Entity> entities = program.getEntities();
        List<EntityGroup> entityGroups = program.getEntityGroups();

        long entityCount = entities.stream().filter(entity -> entity.getName().equals(name)).count();
        long entityGroupCount = entityGroups.stream().filter(entityGroup -> entityGroup.getName().equals(name)).count();

        return entityCount + entityGroupCount == 1;
    }


}
