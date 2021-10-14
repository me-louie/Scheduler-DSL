package ast.transformation;

import ast.SchedulerVisitor;

public class Apply extends Transformation {

    private final String nameSGMG;
    private final String nameEEG;
    private final Integer num;
    private final BitwiseOperator bO;
    private final TimeUnit timeUnit;
    private final String varOrFunc;

    public Apply(String nameSGMG, String nameEEG, Integer num, BitwiseOperator bO, TimeUnit timeUnit, String varOrFunc) {
        this.nameSGMG = nameSGMG;
        this.nameEEG = nameEEG;
        this.num = num;
        this.bO = bO;
        this.timeUnit = timeUnit;
        this.varOrFunc = varOrFunc;
    }

    public Integer getNum() {
        return num;
    }

    public BitwiseOperator getbO() {
        return bO;
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
