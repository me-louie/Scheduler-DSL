package ast.transformations;

import ast.Node;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class LogicalOperator extends Node {

    private final String logicalOperator; 

    public LogicalOperator(String op) {
        this.logicalOperator = op;
    }

    public String getLogicalOperator() {
        return this.logicalOperator;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
