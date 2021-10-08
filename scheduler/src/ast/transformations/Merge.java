package ast.transformations;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Merge extends Transformations{

    private String name;
    private LogicalOperator lO;
    private String nameSGS;
    private String nameMGSS;
    private String nameEEG;

    public Merge(String name, LogicalOperator lO, String nameSGS, String nameMGSS, String nameEEG) {
        this.name = name;
        this.lO = lO;
        this.nameSGS = nameSGS;
        this.nameMGSS = nameMGSS;
        this.nameEEG = nameEEG;
    }

    public String getName() {
        return name;
    }

    public LogicalOperator getlO() {
        return lO;
    }

    public String getNameSGS() {
        return nameSGS;
    }

    public String getNameMGSS() {
        return nameMGSS;
    }

    public String getNameEEG() {
        return nameEEG;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }

}
