package ast.transformation;

import ast.Node;

public abstract class Transformation extends Node {

    public static final String APPLY = "Apply";
    public static final String LOOP = "Loop";
    public static final String MERGE = "Merge";
    public static final String IF_THEN_ELSE = "IfThenElse";

    public abstract String getName();
}
