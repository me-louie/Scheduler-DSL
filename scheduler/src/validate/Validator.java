package validate;

import ast.*;
import ast.transformation.*;

import java.time.LocalDateTime;
import java.util.List;

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
        if (!isUniqueShiftShiftGroupMergeName(shiftName)) {
            throw new DuplicateNameException("2 or more shift/shift group/merge groups share the name " + shiftName);
        } else if (!isValidDateTimeRange(start, end)) {
            throw new InvalidDateTimeRangeException(start + " to " + end + " is not a valid date range.");
        }
    }

    public void validate(ShiftGroup shiftGroup) throws ProgramValidationException {
        String shiftGroupName = shiftGroup.getName();
        List<String> shiftNames = shiftGroup.getShiftList();
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

    public void validate(Apply apply) throws ProgramValidationException {
        String shiftOrShiftGroupName = apply.getNameSGMG();
        String entityOrEntityGroupName = apply.getNameEEG();
        if (!shiftExists(shiftOrShiftGroupName) && !shiftGroupExists(shiftOrShiftGroupName)) {
            throw new NameNotFoundException("There is no shift/shift group named " + shiftOrShiftGroupName);
        } else if (!isUniqueShiftShiftGroupMergeName(shiftOrShiftGroupName)) {
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

    public void validate(Loop loop) throws ProgramValidationException {
        String shiftGroupName = loop.getNameSSG();
        String entityGroupName = loop.getNameEEG();
        if (!shiftGroupExists(shiftGroupName)) {
            throw new NameNotFoundException("There is no shift group named " + shiftGroupName);
        } else if (!isUniqueShiftShiftGroupMergeName(shiftGroupName)) {
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

    private boolean isUniqueShiftShiftGroupMergeName(String name) {
        List<Shift> shifts = program.getShifts();
        // We use shiftGroupsWithoutMergeGroups because the result of a merge gets added to the list of shiftGroups.
        // This way a merge that has already been executed won't be double-counted as a merge and a shift group.
        List<ShiftGroup> shiftGroups = program.getShiftGroupsWithoutMergeGroups();
        List<Transformation> merges = program.transformationMap.get(Transformation.MERGE);

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

    private boolean mergeGroupExists(String name) {
        return program.transformationMap.get(Transformation.MERGE)
                                            .stream().anyMatch(merge -> merge.getName().equals(name));
    }
}
