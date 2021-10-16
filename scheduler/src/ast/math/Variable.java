package ast.math;

import ast.Node;
import ast.SchedulerVisitor;
import validate.ProgramValidationException;
import validate.RedefinitionException;

public class Variable extends Node {

    private final String name;
    private final Integer value;
    private Integer finalValue;
    private final String alias;

    public Variable(String name, Integer value, String alias) {
        this.name = name;
        // A Variable is either a constant value or is the constant value of an alias variable. 1 of these 2 fields is always null.
        this.value = value;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public String getAlias() {
        return alias;
    }

    public Integer getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(Integer finalValue) {
        if (this.finalValue != null) {
            throw new RedefinitionException("The final value of variable " + name + " was attempted to be altered after it was originally set.");
        }
        this.finalValue = finalValue;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
