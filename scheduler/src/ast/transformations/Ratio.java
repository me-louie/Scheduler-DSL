package ast.transformations;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import ast.EntityGroup;
import ast.math.Function;

public class Ratio extends Rule {

    // Note: How does the ratio work ?
    private int ratio;
    private Function func;

    // How do we reference these? Similar question to handling variables
    // Would we have these when we're parsing the rest of the tree?
    private EntityGroup group1;
    private EntityGroup group2;

    public Ratio(String number1, EntityGroup group1, String number2, EntityGroup group2) {
        this.ratio = Math.abs((Integer.parseInt(number1))/(Integer.parseInt(number2)));
        this.group1 = group1;
        this.group2 = group2;
    }

    public int getRatio() {
      return ratio;
    }

    public EntityGroup getGroup1() {
      return group1;
    }

    public EntityGroup getGroup2() {
      return group2;
    }

    public Function getFunc() {
      return func;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
