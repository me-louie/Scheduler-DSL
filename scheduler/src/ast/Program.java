package ast;


import ast.transformation.Transformation;
import validate.ProgramValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program extends Node {

    // given how we're using the Program class it probably makes sense to make these directly accessible
    private final List<Entity> entities;
    private final List<EntityGroup> entityGroups;
    private final List<Shift> shifts;
    private final List<ShiftGroup> shiftGroups;
    private final List<Transformation> transformations;

    public Map<String, Entity> entityMap;
    public Map<String, EntityGroup> entityGroupMap;
    public Map<String, Shift> shiftMap;
    public Map<String, ShiftGroup> shiftGroupMap;
    public Map<String, List<Transformation>> transformationMap;

    private final Header header;

    public Program(Header header, List<Entity> eList, List<EntityGroup> eGroupList, List<Shift> sList, List<ShiftGroup> sgList, List<Transformation> tList, Map<String, Entity> entityMap, Map<String, EntityGroup> entityGroupMap, Map<String, Shift> shiftMap, Map<String, ShiftGroup> shiftGroupMap, Map<String, List<Transformation>> transformationMap) {
        this.entities = eList;
        this.entityGroups = eGroupList;
        this.shifts = sList;
        this.shiftGroups = sgList;
        this.transformations = tList;
        this.header = header;
        this.entityMap = entityMap;
        this.entityGroupMap = entityGroupMap;
        this.shiftMap = shiftMap;
        this.shiftGroupMap = shiftGroupMap;
        this.transformationMap = transformationMap;
    }

    public Map<String, Entity> getEntityMap() {
        return entityMap;
    }

    public Map<String, Shift> getShiftMap() {
        return shiftMap;
    }

    public Map<String, ShiftGroup> getShiftGroupMap() {
        return shiftGroupMap;
    }

    public Map<String, List<Transformation>> getTransformationMap() {
        return transformationMap;
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
