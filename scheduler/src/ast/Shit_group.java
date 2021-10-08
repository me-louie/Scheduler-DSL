package ast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Shit_group extends Node{

    private final String name;

    private List<Shift> shift
        List;


    public Shit_group(String name, List<Shift> shiftList) {
        this.name = name;
        this.shiftList = shiftList;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
