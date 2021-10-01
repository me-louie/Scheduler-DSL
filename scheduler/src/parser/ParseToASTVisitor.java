package parser;

import ast.*;
import parser.SchedulerParser.Schedule_ruleContext;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import java.text.ParseException;

public class ParseToASTVisitor extends AbstractParseTreeVisitor<Node> implements SchedulerParserVisitor<Node> {
    @Override
    public Program visitProgram(SchedulerParser.ProgramContext ctx) {
        Header header = this.visitHeader(ctx.header());
        OperatingHours oHours = this.visitOperating_hours(ctx.operating_hours());
        String oRule;
        if (ctx.operating_rule().OPERATING_RULE_1() != null) {
            oRule = ctx.operating_rule().OPERATING_RULE_1().getText();
        } else if (ctx.operating_rule().OPERATING_RULE_2() != null) {
            oRule = ctx.operating_rule().OPERATING_RULE_2().getText();
        } else {
            throw new RuntimeException("Invalid operating rule");
        }

        Range range = this.visitRange(ctx.range());

        return new Program(null, null, oHours, header, range, oRule, null);
    }

    @Override
    public Header visitHeader(SchedulerParser.HeaderContext ctx) {
        return new Header(ctx.TEXT().getText());
    }

    @Override
    public OperatingHours visitOperating_hours(SchedulerParser.Operating_hoursContext ctx) {
        return new OperatingHours(ctx.TIME(0).getText(), ctx.TIME(1).getText());
    }

    @Override
    public Node visitOperating_rule(SchedulerParser.Operating_ruleContext ctx) {
        return null;
    }

    @Override
    public Range visitRange(SchedulerParser.RangeContext ctx) {
        try {
            return new Range(ctx.DATE(0).getText(), ctx.DATE(1).getText());
        } catch (ParseException e) {
            throw new RuntimeException("Range of schedule: invalid date." + e.getMessage());
        }
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
    public Node visitTimeunit(SchedulerParser.TimeunitContext ctx) {
        return null;
    }

    @Override
    public Node visitDays_of_week(SchedulerParser.Days_of_weekContext ctx) {
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
    public Node visitSpecific_days(SchedulerParser.Specific_daysContext ctx) {
        return null;
    }

    @Override
    public Node visitSpecific_days_by_date(SchedulerParser.Specific_days_by_dateContext ctx) {
        return null;
    }

    @Override
    public Node visitSpecific_days_by_days_of_week(SchedulerParser.Specific_days_by_days_of_weekContext ctx) {
        return null;
    }

    @Override
    public Node visitMin_max_avg_days(SchedulerParser.Min_max_avg_daysContext ctx) {
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
