package ast.transformation;

import ast.SchedulerVisitor;

public class Apply extends Transformation {

    private final String nameSGMG;
    private final String nameEEG;
    private final Integer offsetAmount;
    private final OffsetOperator offsetOperator;
    private final TimeUnit timeUnit;
    private final String varOrFunc;

    public Apply(String nameSGMG, String nameEEG, Integer offsetAmount, OffsetOperator offsetOperator, TimeUnit timeUnit, String varOrFunc) {
        this.nameSGMG = nameSGMG;
        this.nameEEG = nameEEG;
        this.offsetAmount = offsetAmount;
        this.offsetOperator = offsetOperator;
        this.timeUnit = timeUnit;
        this.varOrFunc = varOrFunc;
    }

    public Integer getOffsetAmount() {
        return offsetAmount;
    }

    public OffsetOperator getOffsetOperator() {
        return offsetOperator;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public String getNameSGMG() {
        return nameSGMG;
    }

    public String getNameEEG() {
        return nameEEG;
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
