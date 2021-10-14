package ast.math;

import ast.Node;
import ast.SchedulerVisitor;
import validate.ProgramValidationException;

public class Function extends Node {

    private final String funcName;

    private final String varOrFuncName1;

    private final String varOrFuncName2;
    private final Integer num1;
    private final Integer num2;
    public MathOP mathOP;

    public Function(String funcName, String varOrFuncName1, String varOrFuncName2, MathOP mathOP, Integer num1,
                    Integer num2) {
        this.funcName = funcName;
        this.varOrFuncName1 = varOrFuncName1;
        this.varOrFuncName2 = varOrFuncName2;
        this.mathOP = mathOP;
        this.num1 = num1;
        this.num2 = num2;
    }

    public String getFuncName() {
        return funcName;
    }

    public String getVarOrFuncName1() {
        return varOrFuncName1;
    }

    public String getVarOrFuncName2() {
        return varOrFuncName2;
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
