package ast.transformation;

import ast.SchedulerVisitor;

public class Loop extends Transformation {

    private final String shiftOrShiftGroupOrMergeGroupName;
    private final String entityGroupName;
    private final OffsetOperator offsetOperator;
    private final Integer offsetAmount;
    private final Integer repeatAmount;
    private final String varOrExpression;
    public TimeUnit timeUnit;

    public Loop(String shiftOrShiftGroupOrMergeGroupName, String entityOrEntityGroupName, OffsetOperator offsetOperator, Integer offsetAmount, Integer repeatAmount, String varOrExpression,
                TimeUnit timeUnit) {
        this.shiftOrShiftGroupOrMergeGroupName = shiftOrShiftGroupOrMergeGroupName;
        this.entityGroupName = entityOrEntityGroupName;
        this.offsetOperator = offsetOperator;
        this.offsetAmount = offsetAmount;
        this.repeatAmount = repeatAmount;
        this.varOrExpression = varOrExpression;
        this.timeUnit = timeUnit;
    }

    public String getShiftOrShiftGroupOrMergeGroupName() {
        return shiftOrShiftGroupOrMergeGroupName;
    }

    public String getEntityGroupName() {
        return entityGroupName;
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

    public String getVarOrExpression() {
        return varOrExpression;
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
