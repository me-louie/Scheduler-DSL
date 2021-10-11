package ast.transformation;

import ast.Node;
import ast.SchedulerVisitor;

public class Cond extends Node {

    private LogicalOperator operator;
    private String nameSGMG1;
    private String nameSGMG2;

    public Cond(LogicalOperator operator, String nameSGMG1, String nameSGMG2) {
        this.operator = operator;
        this.nameSGMG1 = nameSGMG1;
        this.nameSGMG2 = nameSGMG2;
    }

    public LogicalOperator getOperator() {
        return operator;
    }

    public String getNameSGMG1() {
        return nameSGMG1;
    }

    public String getNameSGMG2() {
        return nameSGMG2;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }

}
