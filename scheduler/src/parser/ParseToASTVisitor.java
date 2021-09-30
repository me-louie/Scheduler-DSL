package parser;

import ast.*;
import parser.SchedulerParser.Schedule_ruleContext;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class ParseToASTVisitor extends AbstractParseTreeVisitor<Node> implements SchedulerParserVisitor<Node> {
    @Override
    public Node visitProgram(SchedulerParser.ProgramContext ctx) {
        return null;
    }

    @Override
    public Node visitHeader(SchedulerParser.HeaderContext ctx) {
        return null;
    }

    @Override
    public Node visitOperating_hours(SchedulerParser.Operating_hoursContext ctx) {
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
    public Node visitEntity(SchedulerParser.EntityContext ctx) {
        return null;
    }

    @Override
    public Node visitEntity_group(SchedulerParser.Entity_groupContext ctx) {
        return null;
    }

    @Override
    public Node visitName(SchedulerParser.NameContext ctx) {
        return null;
    }

    @Override
    public Node visitEntity_role(SchedulerParser.Entity_roleContext ctx) {
        return null;
    }

    @Override
    public Node visitRules(SchedulerParser.RulesContext ctx) {
        return null;
    }

    @Override
    public Node visitSchedule_rule(Schedule_ruleContext ctx) {
        return null;
    }

    @Override
    public Node visitSchedule(SchedulerParser.ScheduleContext ctx) {
        return null;
    }

    @Override
    public Node visitAvailability(SchedulerParser.AvailabilityContext ctx) {
        return null;
    }

    @Override
    public Node visitFrequency(SchedulerParser.FrequencyContext ctx) {
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
    public Node visitRatio(SchedulerParser.RatioContext ctx) {
        return null;
    }

    @Override
    public Node visitFunction(SchedulerParser.FunctionContext ctx) {
        return null;
    }

    @Override
    public Node visitMath(SchedulerParser.MathContext ctx) {
        return null;
    }

    @Override
    public Node visitExp(SchedulerParser.ExpContext ctx) {
        return null;
    }
}
