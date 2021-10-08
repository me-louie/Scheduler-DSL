package ast.transformations;

import ast.Node;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class BitwiseOperator extends Node {


    private String bO;


    public BitwiseOperator(String bO) {
        this.bO = bO;
    }

    public String getbO() {
        return bO;
    }
    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
