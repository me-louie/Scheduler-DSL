package ast.transformations;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import ast.math.Function;

public class Mandatory extends Rule {

    private final String name;
    // Do we want to allow floats/doubles?
    private int min;
    private int max;
    private int avg;

    private Function func;


    public Mandatory (String name, String min, String max, String avg) {
        this.name = name;
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
        this.avg = Integer.parseInt(avg);
    }

    public String getName() {
      return name;
    }

    public int getMax() {
      return max;
    }

    public int getMin() {
      return min;
    }

    public int getAvg() {
      return avg;
    }

    public Function getFunc() {
      return func;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}