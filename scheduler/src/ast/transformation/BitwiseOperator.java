package ast.transformation;

import ast.Node;
import ast.SchedulerVisitor;

public class BitwiseOperator extends Node {

    private String bO;


    public BitwiseOperator(String bO) {
        this.bO = bO;
    }

    public String getbO() {
        return bO;
    }


    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return null;
    }
}
