package ast;


import ast.math.Function;
import ast.math.Var;
import ast.transformation.Merge;
import ast.transformation.Transformation;
import validate.ProgramValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Program extends Node {

    // Todo: Remove these lists once they're no longer in use
    //       Note that currently the Validator relies on these to check whether an entity/entity group/shift/shift group
    //       has been declared more than once. If we delete these that check will need to be moved to ParseToASTVisitor
    private final List<Entity> entities;
    private final List<EntityGroup> entityGroups;
    private final List<Shift> shifts;
    private final List<ShiftGroup> shiftGroups;
    private final List<ShiftGroup> shiftGroupsWithoutMergeGroups;
    private final List<Transformation> transformations;
    private final List<Merge> mergeList;

    private final List<Function> functionList;
    private final Header header;
    public Map<String, Entity> entityMap;
    public Map<String, EntityGroup> entityGroupMap;
    public Map<String, Shift> shiftMap;
    public Map<String, ShiftGroup> shiftGroupMap;
    public Map<String, List<Transformation>> transformationMap;
    public Map<String, Var> varMaps;
    public Map<String, Function> functionMap;

    public Program(Header header, List<Entity> eList, List<EntityGroup> eGroupList, List<Shift> sList,
                   List<ShiftGroup> sgList, List<Transformation> tList, List<Merge> mergeList,
                   List<Function> functionList, Map<String, Entity> entityMap, Map<String, EntityGroup> entityGroupMap,
                   Map<String, Shift> shiftMap, Map<String, ShiftGroup> shiftGroupMap, Map<String,
            List<Transformation>> transformationMap,
                   Map<String, Var> varMaps, Map<String, Function> functionMap) {
        this.entities = eList;
        this.entityGroups = eGroupList;
        this.shifts = sList;
        this.shiftGroups = sgList;
        this.shiftGroupsWithoutMergeGroups = new ArrayList<>(sgList);
        this.transformations = tList;
        this.header = header;
        this.mergeList = mergeList;
        this.functionList = functionList;
        this.entityMap = entityMap;
        this.entityGroupMap = entityGroupMap;
        this.shiftMap = shiftMap;
        this.shiftGroupMap = shiftGroupMap;
        this.transformationMap = transformationMap;
        this.functionMap = functionMap;
        this.varMaps = varMaps;
    }

    public List<Merge> getMergeList() {
        return mergeList;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<EntityGroup> getEntityGroups() {
        return entityGroups;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public List<ShiftGroup> getShiftGroups() {
        return shiftGroups;
    }

    public List<ShiftGroup> getShiftGroupsWithoutMergeGroups() {
        return shiftGroupsWithoutMergeGroups;
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