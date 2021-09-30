package ast.rules;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Overlap extends Rule {

    // Extend this into a list of pairs/hashtable of names?
    private final String name1;
    private final String name2;

    public Overlap(String name1, String name2){
        this.name1 = name1;
        this.name2= name2;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
