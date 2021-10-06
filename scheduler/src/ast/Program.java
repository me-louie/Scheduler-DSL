package ast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import ast.rules.Rule;

public class Program extends Node {

    private final List<Entity> entities;
    private final List<EntityGroup> entityGroups;
    private final OperatingHours oHours;
    private final Header header;
    private final Range range;
    // Operating rule is essentially two choices, all hours must be scheduled or not
    private final boolean scheduleAllHours;
    private final List<Rule> rules;

    public Program(List<Entity> e, List<EntityGroup> eGroup, OperatingHours oHours, Header header, Range range, String oRule, List<Rule> rules) {
        this.entities = e;
        this.entityGroups = eGroup;
        this.oHours = oHours;
        this.header = header;
        this.range = range;

        if (oRule.equals("All operating hours must be scheduled")) {
            this.scheduleAllHours = true;
        } else if (oRule.equals("Entities can only be scheduled during operating hours, but not all scheduled hours must be used")) {
            this.scheduleAllHours = false;
        } else {
            throw new RuntimeException("Invalid operating rule");
        }
        this.rules = rules;
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

    public Range getRange() {
      return range;
    }

    public List<Rule> getRules() {
      return rules;
    }

    public OperatingHours getOperatingHours() {
      return oHours;
    }

    public boolean shouldScheduleAllHours() {
        return scheduleAllHours;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        //ToDo, print all the statments according to our example.

    }
}
