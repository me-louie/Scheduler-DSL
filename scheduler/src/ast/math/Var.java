package ast.math;

import ast.Node;
import ast.SchedulerVisitor;
import validate.ProgramValidationException;

public class Var extends Node {

    private final String name;

    private final Integer num;

    private final String name2;

    public Var(String name, Integer num, String name2) {
        this.name = name;
        this.num = num;
        this.name2 = name2;
    }

    public String getName() {
        return name;
    }

    public Integer getNum() {
        return num;
    }

    public String getName2() {
        return name2;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return null;
    }
}
