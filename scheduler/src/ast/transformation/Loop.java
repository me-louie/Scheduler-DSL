package ast.transformation;

import ast.SchedulerVisitor;

public class Loop extends Transformation {

    private final String nameSSG;
    private final String nameEEG;
    private final OffsetOperator offsetOperator;
    private final Integer offsetAmount;
    private final Integer repeatAmount;
    private final String varOrFunc;
    public TimeUnit timeUnit;

    public Loop(String nameSSG, String nameEEG, OffsetOperator offsetOperator, Integer offsetAmount, Integer repeatAmount, String varOrFunc,
                TimeUnit timeUnit) {
        this.nameSSG = nameSSG;
        this.nameEEG = nameEEG;
        this.offsetOperator = offsetOperator;
        this.offsetAmount = offsetAmount;
        this.repeatAmount = repeatAmount;
        this.varOrFunc = varOrFunc;
        this.timeUnit = timeUnit;
    }

    public String getNameSSG() {
        return nameSSG;
    }

    public String getNameEEG() {
        return nameEEG;
    }

    public OffsetOperator getOffsetOperator() {
        return offsetOperator;
    }

    public Integer getOffsetAmount() {
        return offsetAmount;
    }

    public Integer getRepeatAmount() {
        return repeatAmount;
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
