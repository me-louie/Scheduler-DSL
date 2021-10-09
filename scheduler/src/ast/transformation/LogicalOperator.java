package ast.transformation;

import ast.Node;
import ast.SchedulerVisitor;

public class LogicalOperator extends Node {

    private final String logicalOperator; 

    public LogicalOperator(String op) {
        this.logicalOperator = op;
    }

    public String getLogicalOperator() {
        return this.logicalOperator;
    }


    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return null;
    }
}
