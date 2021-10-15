package ast;


import ast.math.Expression;
import ast.math.Variable;
import ast.transformation.Transformation;
import validate.ProgramValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Program extends Node {

    private static Program instance;

    public Map<String, Entity> entityMap;
    public Map<String, EntityGroup> entityGroupMap;
    public Map<String, Shift> shiftMap;
    public Map<String, ShiftGroup> shiftGroupMap;
    public Map<String, List<Transformation>> transformationMap;
    public Map<String, Variable> variableMap;
    public Map<String, Expression> expressionMap;
    public final List<ShiftGroup> shiftGroupsWithoutMergeGroups;

    private Program(Map<String, Entity> entityMap, Map<String, EntityGroup> entityGroupMap,
                   Map<String, Shift> shiftMap, Map<String, ShiftGroup> shiftGroupMap, Map<String,
                   List<Transformation>> transformationMap,
                   Map<String, Variable> variableMap, Map<String, Expression> expressionMap) {
        this.entityMap = entityMap;
        this.entityGroupMap = entityGroupMap;
        this.shiftMap = shiftMap;
        this.shiftGroupMap = shiftGroupMap;
        this.shiftGroupsWithoutMergeGroups = new ArrayList<>(shiftGroupMap.values());
        this.transformationMap = transformationMap;
        this.expressionMap = expressionMap;
        this.variableMap = variableMap;
    }

    public static Program getInstance() {
        if (instance == null) {
            throw new RuntimeException("Tried to access Program instance before it had been instantiated.");
        }
        return instance;
    }

    public static void setInstance(Map<String, Entity> entityMap, Map<String, EntityGroup> entityGroupMap,
                                   Map<String, Shift> shiftMap, Map<String, ShiftGroup> shiftGroupMap, Map<String,
                                   List<Transformation>> transformationMap,
                                   Map<String, Variable> varMap, Map<String, Expression> expressionMap) {
        if (instance != null) {
            throw new RuntimeException("Tried to set Program instance after it had already been instantiated.");
        }
        instance = new Program(entityMap, entityGroupMap, shiftMap, shiftGroupMap, transformationMap, varMap, expressionMap);
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}