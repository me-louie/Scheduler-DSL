package ast;


import ast.transformation.Transformation;
import validate.ProgramValidationException;

import java.util.HashMap;
import java.util.List;

public class Program extends Node {

    private final List<Entity> entities;
    private final List<EntityGroup> entityGroups;
    private final List<Shift> shifts;
    private final List<ShiftGroup> shiftGroups;
    private final List<Transformation> transformations;

    private final Header header;

    public Program(Header header, List<Entity> entities, List<EntityGroup> entityGroups, List<Shift> shifts, List<ShiftGroup> shiftGroups, List<Transformation> transformations) {
        this.entities = entities;
        this.entityGroups = entityGroups;
        this.shifts = shifts;
        this.shiftGroups = shiftGroups;
        this.transformations = transformations;
        this.header = header;
    }


    public List<Entity> getEntities() {
      return entities;
    }

    public List<EntityGroup> getEntityGroups() {
      return entityGroups;
    }

    public HashMap<String, EntityGroup> getEntityGroupMap() {
        HashMap<String, EntityGroup> eHashMap = new HashMap<>();
        for (int i =0; i< this.getEntityGroups().size(); i++){
            eHashMap.put(this.entityGroups.get(i).getName(),this.entityGroups.get(i));
        }

        return eHashMap;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public List<ShiftGroup> getShiftGroups() {
        return shiftGroups;
    }

    public List<Transformation> getTransformations() {
        return transformations;
    }

    public Header getHeader() {
      return header;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
