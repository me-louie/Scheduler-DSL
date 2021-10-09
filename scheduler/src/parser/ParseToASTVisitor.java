package parser;

import ast.*;

import ast.transformation.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseToASTVisitor extends AbstractParseTreeVisitor<Node> implements SchedulerParserVisitor<Node> {
    @Override
    public Program visitProgram(SchedulerParser.ProgramContext ctx) {
        Header header = this.visitHeader(ctx.header());

        List<EntityGroup> eGroupList = new ArrayList<>();

        List<Entity> eList = new ArrayList<>();
        List<Shift> sList = new ArrayList<>();
        List<ShiftGroup> sgList = new ArrayList<>();
        List<Transformation> tList = new ArrayList<>();

        Map<String, Entity> entityMap = new HashMap<>();
        Map<String, EntityGroup> entityGroupMap = new HashMap<>();
        Map<String, Shift> shiftMap = new HashMap<>();
        Map<String, ShiftGroup> shiftGroupMap = new HashMap<>();
        Map<String, List<Transformation>> transformationMap = new HashMap<>();

        for (SchedulerParser.EntityContext e1 : ctx.entity()) {
            Entity entity = this.visitEntity(e1);
            eList.add(entity);
            entityMap.put(entity.getName(), entity);
        }

        for (SchedulerParser.Entity_groupContext e : ctx.entity_group()) {
            EntityGroup entityGroup = this.visitEntity_group(e);
            eGroupList.add(entityGroup);
            entityGroupMap.put(entityGroup.getName(), entityGroup);
        }

        for (SchedulerParser.ShiftContext s : ctx.shift()) {
            Shift shift = this.visitShift(s);
            sList.add(shift);
            shiftMap.put(shift.getName(), shift);
        }

        for (SchedulerParser.Shift_groupContext e : ctx.shift_group()) {
            ShiftGroup shiftGroup = this.visitShift_group(e);
            sgList.add(shiftGroup);
            shiftGroupMap.put(shiftGroup.getName(), shiftGroup);
        }

        for (SchedulerParser.TransformationsContext e : ctx.transformations()) {
            if (e.apply() != null) {
                Apply apply = this.visitApply(e.apply());
                tList.add(apply);
                if (!transformationMap.containsKey(Transformation.APPLY)) {
                    transformationMap.put(Transformation.APPLY, new ArrayList<>());
                }
                transformationMap.get(Transformation.APPLY).add(apply);
            } else if (e.merge() != null){
                Merge merge = this.visitMerge(e.merge());
                tList.add(merge);
                if (!transformationMap.containsKey(Transformation.MERGE)) {
                    transformationMap.put(Transformation.MERGE, new ArrayList<>());
                }
                transformationMap.get(Transformation.MERGE).add(merge);
            } else if (e.loop() != null){
                Loop loop = this.visitLoop(e.loop());
                tList.add(loop);
                if (!transformationMap.containsKey(Transformation.LOOP)) {
                    transformationMap.put(Transformation.LOOP, new ArrayList<>());
                }
                transformationMap.get(Transformation.LOOP).add(loop);

            }
        }

        return new Program(header, eList, eGroupList, sList, sgList, tList, entityMap, entityGroupMap, shiftMap, shiftGroupMap, transformationMap);
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
            bO = getBitwiseOperator(ctx.bitwise_operator().getText());
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
        LogicalOperator lO = getLogicalOperator(ctx.logical_operator().getText());
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
        BitwiseOperator bO = getBitwiseOperator(ctx.bitwise_operator().getText());
        System.out.println(num);
        System.out.println(repNum);
        System.out.println(ctx.bitwise_operator().getText());
        return new Loop(shiftOrShiftGroupOrMergeName,entityOrEntityGroupName,bO,num,repNum);
    }

    private BitwiseOperator getBitwiseOperator(String operator) {
        return switch (operator.trim()) { // hack to make this work for now, for some reason these are coming out with WS (e.g. "AND ")
            case "<<" -> BitwiseOperator.LEFTSHIFT;
            case ">>" -> BitwiseOperator.RIGHTSHIFT;
            default -> throw new RuntimeException("Unrecognized bitwise operator");
        };
    }

    private LogicalOperator getLogicalOperator(String operator) {
        return switch (operator.trim()) {
            case "AND" -> LogicalOperator.AND;
            case "OR" -> LogicalOperator.OR;
            case "XOR" -> LogicalOperator.XOR;
            default -> throw new RuntimeException("Unrecognized logical operator");
        };
    }
}
