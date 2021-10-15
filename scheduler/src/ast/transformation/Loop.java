package ast.transformation;

import ast.SchedulerVisitor;

public class Loop extends Transformation {

    private final String nameSSG;
    private final String nameEEG;
    private final BitwiseOperator b0;
    private final Integer num;
    private final Integer repNum;
    private final String varOrFunc;
    public TimeUnit timeUnit;

    public Loop(String nameSSG, String nameEEG, BitwiseOperator b0, Integer num, Integer repNum, String varOrFunc,
                TimeUnit timeUnit) {
        this.nameSSG = nameSSG;
        this.nameEEG = nameEEG;
        this.b0 = b0;
        this.num = num;
        this.repNum = repNum;
        this.varOrFunc = varOrFunc;
        this.timeUnit = timeUnit;
    }

    public String getNameSSG() {
        return nameSSG;
    }

    public String getNameEEG() {
        return nameEEG;
    }

    public BitwiseOperator getB0() {
        return b0;
    }

    public Integer getNum() {
        return num;
    }

    public Integer getRepNum() {
        return repNum;
    }

    public String getVarOrFunc() {
        return varOrFunc;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }

    @Override
    public String getName() {
        return null;
    }
}
