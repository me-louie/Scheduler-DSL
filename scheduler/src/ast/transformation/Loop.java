package ast.transformation;

import ast.SchedulerVisitor;

public class Loop extends Transformation {

    private String nameSSG;
    private String nameEEG;
    private BitwiseOperator b0;
    private Integer num;
    private Integer repNum;
    private String varOrfunc;

    public Loop(String nameSSG, String nameEEG, BitwiseOperator b0, Integer num, Integer repNum, String varOrfunc) {
        this.nameSSG = nameSSG;
        this.nameEEG = nameEEG;
        this.b0 = b0;
        this.num = num;
        this.repNum = repNum;
        this.varOrfunc = varOrfunc;
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

    public String getVarOrfunc() {
        return varOrfunc;
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
