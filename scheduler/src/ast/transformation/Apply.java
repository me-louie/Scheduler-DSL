package ast.transformation;

import ast.SchedulerVisitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Apply extends Transformation {

    private final String nameSGMG;
    private final String nameEEG;
    private final Integer num;
    private final BitwiseOperator bO;
    private final TimeUnit timeUnit;
    private final String varOrfunc;

    public Apply(String nameSGMG, String nameEEG, Integer num, BitwiseOperator bO, TimeUnit timeUnit, String varOrfunc) {
        this.nameSGMG = nameSGMG;
        this.nameEEG = nameEEG;
        this.num = num;
        this.bO = bO;
        this.timeUnit = timeUnit;
        this.varOrfunc = varOrfunc;
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
