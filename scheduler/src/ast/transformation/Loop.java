package ast.transformation;

import ast.SchedulerVisitor;

public class Loop extends Transformation {

    private String nameSSG;
    private String nameEEG;
    private BitwiseOperator b0;
    private Integer num;
    private Integer repNum;

    public Loop(String nameSSG, String nameEEG, BitwiseOperator b0, Integer num, Integer repNum) {
        this.nameSSG = nameSSG;
        this.nameEEG = nameEEG;
        this.b0 = b0;
        this.num = num;
        this.repNum = repNum;
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

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }

    @Override
    public String getName() {
        return null;
    }
}
