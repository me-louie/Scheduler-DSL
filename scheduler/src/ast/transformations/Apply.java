package ast.transformations;

import ast.Entity;
import ast.EntityGroup;
import ast.Shift;
import ast.Shift_group;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Apply extends Transformations{


    

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

    public String getNameSGMG() {
        return nameSGMG;
    }

    public String getNameEEG() {
        return nameEEG;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        
    }
}
