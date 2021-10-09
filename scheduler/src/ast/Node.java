package ast;


import validate.ProgramValidationException;

import java.io.*;

public abstract class Node {
    abstract public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException; // so that we remember to define this in all subclasses

}

