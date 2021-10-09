package ast.transformation;

import ast.SchedulerVisitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Apply extends Transformation {

    private String nameSGMG;
    private String nameEEG;
    private Integer num;
    private BitwiseOperator bO;

    public Apply(String nameSGMG, String nameEEG, Integer num, BitwiseOperator bO) {
        this.nameSGMG = nameSGMG;
        this.nameEEG = nameEEG;
        this.num = num;
        this.bO = bO;
    }

    public Integer getNum() {
        return num;
    }

    public BitwiseOperator getbO() {
        return bO;
    }

    public String getNameSGMG() {
        return nameSGMG;
    }

    public String getNameEEG() {
        return nameEEG;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }
}
