package parser;

import ast.*;
import ast.rules.*;
import parser.SchedulerParser.Schedule_ruleContext;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import java.util.ArrayList;
import java.util.List;

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

        List<EntityGroup> eGroupList = new ArrayList<>();

        List<Entity> eList = new ArrayList<>();

        for (SchedulerParser.EntityContext e1 : ctx.entity()) {
            eList.add(this.visitEntity(e1));
        }

        for (SchedulerParser.Entity_groupContext e : ctx.entity_group()) {
            eGroupList.add(this.visitEntity_group(e));
        }

        List<SchedulerParser.Schedule_ruleContext> rulesCtx = ctx.rules().schedule_rule();
        List<Rule> rules = new ArrayList<>();

        for (SchedulerParser.Schedule_ruleContext ruleCtx : rulesCtx) {
            if (ruleCtx.schedule() != null) {
                rules.add(this.visitSchedule(ruleCtx.schedule()));
            } else if (ruleCtx.overlap() != null){
                rules.add(this.visitOverlap(ruleCtx.overlap()));
            }  else if (ruleCtx.availability() != null){
                rules.add(this.visitAvailability(ruleCtx.availability()));
            }  else if (ruleCtx.frequency() != null){
                rules.add(this.visitFrequency(ruleCtx.frequency()));
            } else if (ruleCtx.ratio() != null){
                rules.add(this.visitRatio(ruleCtx.ratio()));
            } else {
                throw new RuntimeException("Invalid rule");
            }
        }

        return new Program(eList, eGroupList, oHours, header, range, oRule, rules);
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
        return new Range(ctx.DATE(0).getText(), ctx.DATE(1).getText());
    }

    @Override
    public Entity visitEntity(SchedulerParser.EntityContext ctx) {
        return new Entity(ctx.name().getText());
    }

    @Override
    public EntityGroup visitEntity_group(SchedulerParser.Entity_groupContext ctx) {

        String name = ctx.name(0).getText();
        List<Entity> elist = new ArrayList<>();

        //System.out.println("HELLO");
        //System.out.println(ctx.name(0).getText());
        for(int i = 1; i < ctx.name().size();i++){
            elist.add(new Entity(ctx.name(i).getText()));
            //System.out.println(ctx.name(i).getText());
        }
        return new EntityGroup(name,elist);
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
    public Schedule visitSchedule(SchedulerParser.ScheduleContext ctx) {
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
    public Availability visitAvailability(SchedulerParser.AvailabilityContext ctx) {
        return null;
    }

    @Override
    public Frequency visitFrequency(SchedulerParser.FrequencyContext ctx) {
        return null;
    }

    @Override
    public Overlap visitOverlap(SchedulerParser.OverlapContext ctx) {
        return new Overlap(ctx.name(0).getText(), ctx.name(1).getText());
    }

    @Override
    public Ratio visitRatio(SchedulerParser.RatioContext ctx) {
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
