package ast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {

    }
}
