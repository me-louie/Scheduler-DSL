package parser;

import ast.*;

import ast.transformation.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import java.util.ArrayList;
import java.util.List;

public class ParseToASTVisitor extends AbstractParseTreeVisitor<Node> implements SchedulerParserVisitor<Node> {
    @Override
    public Program visitProgram(SchedulerParser.ProgramContext ctx) {
        Header header = this.visitHeader(ctx.header());

        List<EntityGroup> eGroupList = new ArrayList<>();

        List<Entity> eList = new ArrayList<>();
        List<Shift> sList = new ArrayList<>();
        List<ShiftGroup> sgList = new ArrayList<>();
        List<Transformation> tList = new ArrayList<>();

        for (SchedulerParser.EntityContext e1 : ctx.entity()) {
            eList.add(this.visitEntity(e1));
        }

        for (SchedulerParser.Entity_groupContext e : ctx.entity_group()) {
            eGroupList.add(this.visitEntity_group(e));
        }

        for (SchedulerParser.ShiftContext s : ctx.shift()) {
            sList.add(this.visitShift(s));
        }

        for (SchedulerParser.Shift_groupContext e : ctx.shift_group()) {
            sgList.add(this.visitShift_group(e));
        }
        for (SchedulerParser.Shift_groupContext e : ctx.shift_group()) {
            sgList.add(this.visitShift_group(e));
        }

        for (SchedulerParser.TransformationsContext e : ctx.transformations()) {
            if (e.apply() != null){
                tList.add(this.visitApply(e.apply()));
            } else if (e.merge() != null){
                tList.add(this.visitMerge(e.merge()));
            } else if (e.loop() != null){
                tList.add(this.visitLoop(e.loop()));
            }
        }

        return new Program(header, eList, eGroupList, sList, sgList, tList);
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
       List<String> eList = new ArrayList<>();

       for(int i = 1; i < ctx.name().size(); i++){
           eList.add((ctx.name(i).getText()));
       }

        return new EntityGroup(name, eList);
    }

    @Override
    public Node visitName(SchedulerParser.NameContext ctx) {
        return null;
    }

    @Override
    public Shift visitShift(SchedulerParser.ShiftContext ctx) {
        String name = ctx.name().getText();
        System.out.println(ctx.TIME(0).getText());
        System.out.println(ctx.DATE(1).getText());
        String open = ctx.DATE(0).getText() + ctx.TIME(0).getText();
        String close = ctx.DATE(1).getText() + ctx.TIME(1).getText();

        return new Shift(name, open, close);
    }

    @Override
    public ShiftGroup visitShift_group(SchedulerParser.Shift_groupContext ctx) {

        String name = ctx.name(0).getText();
        List<String> sList = new ArrayList<>();

        for(int i = 1; i < ctx.name().size(); i++){
            sList.add((ctx.name(i).getText()));
            System.out.println(ctx.name(i).getText());
        }


        return new ShiftGroup(name, sList);


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
    public Apply visitApply(SchedulerParser.ApplyContext ctx) {

        String shiftOrShiftGroupOrMergeName = ctx.name(0).getText();
        String entityOrEntityGroupName = ctx.name(1).getText();
        Integer num = null;
        if (ctx.NUM() != null){
            num = Integer.parseInt(ctx.NUM().getText());
        }
        BitwiseOperator bO = null;
        if (ctx.bitwise_operator() != null){
            bO = new BitwiseOperator(ctx.bitwise_operator().getText());
        }
        System.out.println(num);
        System.out.println(bO);
        return new Apply(shiftOrShiftGroupOrMergeName,entityOrEntityGroupName,num, bO);
    }

    @Override
    public Merge visitMerge(SchedulerParser.MergeContext ctx) {
        String name = ctx.name(0).getText();
        String shiftOrShiftGroup1 = ctx.name(1).getText();
        String shiftOrShiftGroup2 = ctx.name(2).getText();
        LogicalOperator lO = new LogicalOperator(ctx.logical_operator().getText());
        return new Merge(name, lO, shiftOrShiftGroup1, shiftOrShiftGroup2);
    }

    @Override
    public Loop visitLoop(SchedulerParser.LoopContext ctx) {
        String shiftOrShiftGroupOrMergeName = ctx.name(0).getText();
        String entityOrEntityGroupName = ctx.name(1).getText();
        Integer num = Integer.parseInt(ctx.NUM(0).getText());
        Integer repNum = null;
        if (ctx.NUM(1) !=null){
            repNum = Integer.parseInt(ctx.NUM(1).getText());
        }
        BitwiseOperator bO = new BitwiseOperator(ctx.bitwise_operator().getText());
        System.out.println(num);
        System.out.println(repNum);
        System.out.println(ctx.bitwise_operator().getText());
        return new Loop(shiftOrShiftGroupOrMergeName,entityOrEntityGroupName,bO,num,repNum);
    }
}
