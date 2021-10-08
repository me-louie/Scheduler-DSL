package ast;

public class Entity extends Node {
    private final String name;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }
}
