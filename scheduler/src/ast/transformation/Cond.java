package ast.transformation;

import ast.Node;
import ast.SchedulerVisitor;

public class Cond extends Node {

    private SetOperator setOperator;
    private String shiftGroupOrMergeGroupName1;
    private String shiftGroupOrMergeGroupName2;
    private boolean state;

    public Cond(SetOperator setOperator, String shiftGroupOrMergeGroupName1, String shiftGroupOrMergeGroupName2) {
        this.setOperator = setOperator;
        this.shiftGroupOrMergeGroupName1 = shiftGroupOrMergeGroupName1;
        this.shiftGroupOrMergeGroupName2 = shiftGroupOrMergeGroupName2;
    }

    public SetOperator getSetOperator() {
        return setOperator;
    }

    public String getShiftGroupOrMergeGroupName1() {
        return shiftGroupOrMergeGroupName1;
    }

    public String getShiftGroupOrMergeGroupName2() {
        return shiftGroupOrMergeGroupName2;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }

}
