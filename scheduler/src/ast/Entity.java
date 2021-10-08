package ast;

public class Entity extends Node {
    private final String name;
    // TOD: Add entity groups after changes from user studies have been finalized.

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
