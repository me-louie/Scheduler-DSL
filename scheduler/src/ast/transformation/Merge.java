package ast.transformation;

import ast.SchedulerVisitor;

public class Merge extends Transformation {

    private final String name;
    private final LogicalOperator lO;
    private final String nameSGS1;
    private final String nameSGS2;

    public String getNameSGS1() {
        return nameSGS1;
    }

    public String getNameSGS2() {
        return nameSGS2;
    }

    public Merge(String name, LogicalOperator lO, String nameSGS1, String nameSGS2) {
        this.name = name;
        this.lO = lO;
        this.nameSGS1 = nameSGS1;
        this.nameSGS2 = nameSGS2;
    }

    public String getName() {
        return name;
    }

    public LogicalOperator getlO() {
        return lO;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }
}
