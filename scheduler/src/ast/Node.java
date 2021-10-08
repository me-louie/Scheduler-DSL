package ast;


import java.io.*;

public abstract class Node {
    abstract public <T> T accept(SchedulerVisitor<T> v); // so that we remember to define this in all subclasses

}

