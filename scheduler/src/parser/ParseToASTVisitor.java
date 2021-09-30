package parser;

import ast.*;
import ast.rules.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class ParseToASTVisitor extends AbstractParseTreeVisitor<Node> implements SchedulerParserVisitor<Node> {
    @Override
    public Program visitProgram(SchedulerParser.ProgramContext ctx) {
        return null;
    }

    @Override
    public Header visitHeader(SchedulerParser.HeaderContext ctx) {
        return null;
    }

    @Override
    public OperatingHours visitOperating_hours(SchedulerParser.Operating_hoursContext ctx) {
        return null;
    }

    @Override
    public Node visitOperating_rule(SchedulerParser.Operating_ruleContext ctx) {
        return null;
    }

    @Override
    public Node visitRange(SchedulerParser.RangeContext ctx) {
        return null;
    }

    @Override
    public Entity visitEntity(SchedulerParser.EntityContext ctx) {
        return null;
    }

    @Override
    public EntityGroup visitEntity_group(SchedulerParser.Entity_groupContext ctx) {
        return null;
    }

    @Override
    public Node visitRules(SchedulerParser.RulesContext ctx) {
        return null;
    }

    @Override
    public Rule visitRule(SchedulerParser.RuleContext ctx) {
        return null;
    }

    @Override
    public Schedule visitSchedule(SchedulerParser.ScheduleContext ctx) {
        return null;
    }

    @Override
    public Availability visitAvailability(SchedulerParser.AvailabilityContext ctx) {
        return null;
    }

    @Override
    public Frequency visitFrequency(SchedulerParser.FrequencyContext ctx) {
        return null;
    }

    @Override
    public Node visitMandatory(SchedulerParser.MandatoryContext ctx) {
        return null;
    }

    @Override
    public Node visitOverlap(SchedulerParser.OverlapContext ctx) {
        return null;
    }

    @Override
    public Ratio visitRatio(SchedulerParser.RatioContext ctx) {
        return null;
    }

    @Override
    public Node visitFunction(SchedulerParser.FunctionContext ctx) {
        return null;
    }
}
