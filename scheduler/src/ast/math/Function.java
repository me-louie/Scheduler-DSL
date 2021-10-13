package ast.math;

import ast.Node;
import ast.SchedulerVisitor;
import validate.ProgramValidationException;

import java.util.List;

public class Function extends Node {

    private final String funcName;

    private final String varorfuncName1;

    private final String varorfuncName2;

    public MathOP mathOP;

    private final Integer num1;

    private final Integer num2;

    public Function(String funcName, String varorfuncName1, String varorfuncName2, MathOP mathOP, Integer num1, Integer num2) {
        this.funcName = funcName;
        this.varorfuncName1 = varorfuncName1;
        this.varorfuncName2 = varorfuncName2;
        this.mathOP = mathOP;
        this.num1 = num1;
        this.num2 = num2;
    }

    public String getFuncName() {
        return funcName;
    }

    public String getVarorfuncName1() {
        return varorfuncName1;
    }

    public String getVarorfuncName2() {
        return varorfuncName2;
    }

    public Integer getNum1() {
        return num1;
    }

    public Integer getNum2() {
        return num2;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return null;
    }
}
