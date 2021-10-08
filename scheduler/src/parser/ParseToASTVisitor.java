package parser;

import ast.*;
import ast.transformations.*;
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
    public Node visitShift(SchedulerParser.ShiftContext ctx) {
        return null;
    }

    @Override
    public Node visitShift_group(SchedulerParser.Shift_groupContext ctx) {
        return null;
    }

    @Override
    public Node visitLogical_operator(SchedulerParser.Logical_operatorContext ctx) {
        return null;
    }

    @Override
    public Node visitBitwise_operator(SchedulerParser.Bitwise_operatorContext ctx) {
        return null;
    }

    @Override
    public Node visitTransformations(SchedulerParser.TransformationsContext ctx) {
        return null;
    }

    @Override
    public Node visitApply(SchedulerParser.ApplyContext ctx) {
        return null;
    }

    @Override
    public Node visitMerge(SchedulerParser.MergeContext ctx) {
        return null;
    }

    @Override
    public Node visitLoop(SchedulerParser.LoopContext ctx) {
        return null;
    }
}
