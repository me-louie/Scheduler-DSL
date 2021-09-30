package ast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import ast.rules.Rule;

public class Program extends Node {

    private final Entity entity;
    private final EntityGroup entityGroup;
    private final OperatingHours oHours;
    private final Header header;
    private final Range range;
    // Operating rule is essentially two choices, all hours must be scheduled or not
    private final boolean scheduleAllHours;
    private final List<Rule> rules;

    public Program(Entity e, EntityGroup eGroup, OperatingHours oHours, Header header, Range range, String oRule, List<Rule> rules) {
        this.entity = e;
        this.entityGroup = eGroup;
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

    public Entity getEntity() {
      return entity;
    }

    public EntityGroup getEntityGroup() {
      return entityGroup;
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
