package ast.transformation;

import ast.SchedulerVisitor;

public class Apply extends Transformation {

    private final String shiftOrShiftGroupOrMergeGroupName;
    private final String entityOrEntityGroupName;
    private final Integer offsetAmount;
    private final OffsetOperator offsetOperator;
    private final TimeUnit timeUnit;
    private final String varOrExpression;
    private final Integer repeatAmount;

    public Apply(String shiftOrShiftGroupOrMergeGroupName, String entityOrEntityGroupName, Integer offsetAmount,
                 OffsetOperator offsetOperator, TimeUnit timeUnit, String varOrExpression, Integer repeatAmount) {
        this.shiftOrShiftGroupOrMergeGroupName = shiftOrShiftGroupOrMergeGroupName;
        this.entityOrEntityGroupName = entityOrEntityGroupName;
        this.offsetAmount = offsetAmount;
        this.offsetOperator = offsetOperator;
        this.timeUnit = timeUnit;
        this.varOrExpression = varOrExpression;
        this.repeatAmount = repeatAmount;
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

    public String getShiftOrShiftGroupOrMergeGroupName() {
        return shiftOrShiftGroupOrMergeGroupName;
    }

    public String getEntityOrEntityGroupName() {
        return entityOrEntityGroupName;
    }

    public String getVarOrExpression() {
        return varOrExpression;
    }

    public Integer getRepeatAmount() {
        return repeatAmount;
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
