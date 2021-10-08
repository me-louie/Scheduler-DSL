package ast;

import java.util.List;

public class EntityGroup extends Node {
    private final String name;
    private final List<Entity> members;

    public EntityGroup(String name, List<Entity> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public List<Entity> getMembers() {
        return members;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }
}
