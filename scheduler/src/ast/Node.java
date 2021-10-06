package ast;


import java.io.*;

public abstract class Node {
    abstract public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException;
    // abstract public <T> T accept(ScheduleVi<T> writer);
}

