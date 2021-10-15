package ast;

import validate.ProgramValidationException;

import java.util.List;

public class EntityGroup extends Node {

    private final String name;
    private final List<String> entities;

    public EntityGroup(String name, List<String> members) {
        this.name = name;
        this.entities = members;
    }

    public String getName() {
        return name;
    }

    public List<String> getEntities() {
        return entities;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }
}
