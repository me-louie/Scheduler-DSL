package ast.transformation;

import ast.Node;
import ast.SchedulerVisitor;

public class Cond extends Node {

    private SetOperator operator;
    private String nameSGMG1;
    private String nameSGMG2;
    private boolean state;

    public Cond(SetOperator operator, String nameSSG1, String nameSSG2) {
        this.operator = operator;
        this.nameSGMG1 = nameSSG1;
        this.nameSGMG2 = nameSSG2;
    }

    public SetOperator getOperator() {
        return operator;
    }

    public String getShiftGroupOrMergeGroupName1() {
        return nameSGMG1;
    }

    public String getShiftGroupOrMergeGroupName2() {
        return nameSGMG2;
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
