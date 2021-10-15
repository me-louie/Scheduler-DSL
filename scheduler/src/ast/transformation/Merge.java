package ast.transformation;

import ast.SchedulerVisitor;

public class Merge extends Transformation {

    private final String name;
    private final SetOperator setOperator;
    private final String shiftGroupOrMergeGroupName1;
    private final String shiftGroupOrMergeGroupName2;

    public String getShiftGroupOrMergeGroupName1() {
        return shiftGroupOrMergeGroupName1;
    }

    public String getShiftGroupOrMergeGroupName2() {
        return shiftGroupOrMergeGroupName2;
    }

    public Merge(String name, SetOperator setOperator, String shiftGroupOrMergeGroupName1, String shiftGroupOrMergeGroupName2) {
        this.name = name;
        this.setOperator = setOperator;
        this.shiftGroupOrMergeGroupName1 = shiftGroupOrMergeGroupName1;
        this.shiftGroupOrMergeGroupName2 = shiftGroupOrMergeGroupName2;
    }

    public String getName() {
        return name;
    }

    public SetOperator getSetOperator() {
        return setOperator;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }
}
