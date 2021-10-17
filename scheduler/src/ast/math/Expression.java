package ast.math;

import ast.Node;
import ast.SchedulerVisitor;
import validate.ProgramValidationException;
import validate.RedefinitionException;

public class Expression extends Node {

    private final String name;
    private final String variableOrExpressionName1;
    private final String variableOrExpressionName2;
    private final Integer value1;
    private final Integer value2;
    private Integer finalValue;
    public MathOperation mathOperation;

    public Expression(String name, String variableOrExpressionName1, String variableOrExpressionName2, MathOperation mathOperation, Integer value1,
                      Integer value2) {
        this.name = name;
        this.variableOrExpressionName1 = variableOrExpressionName1;
        this.variableOrExpressionName2 = variableOrExpressionName2;
        this.mathOperation = mathOperation;
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getName() {
        return name;
    }

    public String getVariableOrExpressionName1() {
        return variableOrExpressionName1;
    }

    public String getVariableOrExpressionName2() {
        return variableOrExpressionName2;
    }

    public Integer getValue1() {
        return value1;
    }

    public Integer getValue2() {
        return value2;
    }

    public Integer getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(Integer finalValue) {
        if (this.finalValue != null) {
            throw new RedefinitionException("The final value of expression " + name + " was attempted to be altered after it was originally set.");
        }
        this.finalValue = finalValue;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
