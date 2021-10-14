package ast.transformation;

import ast.Node;
import ast.SchedulerVisitor;

public class Cond extends Node {

    private final LogicalOperator operator;
    private final String nameSSG1;
    private final String nameSSG2;
    private boolean state;

    public Cond(LogicalOperator operator, String nameSSG1, String nameSSG2) {
        this.operator = operator;
        this.nameSSG1 = nameSSG1;
        this.nameSSG2 = nameSSG2;
    }

    public LogicalOperator getOperator() {
        return operator;
    }

    public String getNameSSG1() {
        return nameSSG1;
    }

    public String getNameSSG2() {
        return nameSSG2;
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
