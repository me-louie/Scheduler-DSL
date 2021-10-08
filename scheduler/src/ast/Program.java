package ast;


import ast.transformations.Transformations;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

public class Program extends Node {

    private final List<Entity> entities;
    private final List<EntityGroup> entityGroups;
    private final List<Shift> shifts;
    private final List<Shift_group> shiftGroups;
    private final List<Transformations> transformations;

    private final Header header;

    public Program(Header header, List<Entity> entities, List<EntityGroup> entityGroups, List<Shift> shifts, List<Shift_group> shiftGroups, List<Transformations> transformations) {
        this.entities = entities;
        this.entityGroups = entityGroups;
        this.shifts = shifts;
        this.shiftGroups = shiftGroups;
        this.transformations = transformations;
        this.header = header;
    }


    public List<Entity> getEntity() {
      return entities;
    }

    public List<EntityGroup> getEntityGroup() {
      return entityGroups;
    }

    public HashMap<String, EntityGroup> getEntityGroupMap() {
        HashMap<String, EntityGroup> eHashMap = new HashMap<>();
        for (int i =0; i< this.getEntityGroup().size(); i++){
            eHashMap.put(this.entityGroups.get(i).getName(),this.entityGroups.get(i));
        }

        return eHashMap;
    }

    public Header getHeader() {
      return header;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) {
        return v.visit(this);
    }
}
