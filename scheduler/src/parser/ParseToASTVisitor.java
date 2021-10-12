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
        List<Apply> applyList = new ArrayList<>();
        List<Merge> mergeList = new ArrayList<>();
        List<Loop> loopList = new ArrayList<>();

        Map<String, Entity> entityMap = new HashMap<>();
        Map<String, EntityGroup> entityGroupMap = new HashMap<>();
        Map<String, Shift> shiftMap = new HashMap<>();
        Map<String, ShiftGroup> shiftGroupMap = new HashMap<>();
        Map<String, List<Transformation>> transformationMap = new HashMap<>(){{
            put(Transformation.APPLY, new ArrayList<>());
            put(Transformation.MERGE, new ArrayList<>());
            put(Transformation.LOOP, new ArrayList<>());
            put(Transformation.IF_THEN_ELSE, new ArrayList<>());
        }};

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

        for (SchedulerParser.TransformationContext e : ctx.transformation()) {
            if (e.apply() != null) {
                Apply apply = this.visitApply(e.apply());
                tList.add(apply);
                transformationMap.get(Transformation.APPLY).add(apply);
            } else if (e.merge() != null) {
                Merge merge = this.visitMerge(e.merge());
                tList.add(merge);
                mergeList.add(merge);
                transformationMap.get(Transformation.MERGE).add(merge);
            } else if (e.loop() != null) {
                Loop loop = this.visitLoop(e.loop());
                tList.add(loop);
                transformationMap.get(Transformation.LOOP).add(loop);
            } else if (e.ifthenelse() != null) {
                IfThenElse ifThenElse = this.visitIfthenelse(e.ifthenelse());
                tList.add(ifThenElse);
                transformationMap.get(Transformation.IF_THEN_ELSE).add(ifThenElse);
                // Need to add nested transformations to map for validation checks
                addNestedTransformations(transformationMap, ifThenElse.getThenTransformations());
                addNestedTransformations(transformationMap, ifThenElse.getElseTransformations());
            }
        }

        return new Program(header, eList, eGroupList, sList, sgList, tList, mergeList, entityMap, entityGroupMap,
                shiftMap, shiftGroupMap, transformationMap);
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

        for (int i = 1; i < ctx.name().size(); i++) {
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

        for (int i = 1; i < ctx.name().size(); i++) {
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
    public Node visitTransformation(SchedulerParser.TransformationContext ctx) {
        return null;
    }

    @Override
    public Node visitCond_transformations(SchedulerParser.Cond_transformationsContext ctx) {
        return null;
    }

    @Override
    public Apply visitApply(SchedulerParser.ApplyContext ctx) {
        String shiftOrShiftGroupOrMergeName = ctx.name(0).getText();
        String entityOrEntityGroupName = ctx.name(1).getText();
        Integer num = null;
        if (ctx.NUM() != null) {
            num = Integer.parseInt(ctx.NUM().getText());
        }
        BitwiseOperator bO = null;
        if (ctx.bitwise_operator() != null) {
            bO = getBitwiseOperator(ctx.bitwise_operator().getText());
        }
        System.out.println(num);
        System.out.println(bO);
        return new Apply(shiftOrShiftGroupOrMergeName, entityOrEntityGroupName, num, bO);
    }

    @Override
    public Merge visitMerge(SchedulerParser.MergeContext ctx) {
        String name = ctx.name(0).getText();
        String shiftOrShiftGroup1 = ctx.name(1).getText();
        String shiftOrShiftGroupOrMerge = ctx.name(2).getText();
        LogicalOperator lO = getLogicalOperator(ctx.logical_operator().getText());
        return new Merge(name, lO, shiftOrShiftGroup1, shiftOrShiftGroupOrMerge);
    }

    @Override
    public Loop visitLoop(SchedulerParser.LoopContext ctx) {
        String shiftOrShiftGroupOrMergeName = ctx.name(0).getText();
        String entityOrEntityGroupName = ctx.name(1).getText();
        Integer num = Integer.parseInt(ctx.NUM(0).getText());
        Integer repNum = null;
        if (ctx.NUM(1) != null) {
            repNum = Integer.parseInt(ctx.NUM(1).getText());
        }
        BitwiseOperator bO = getBitwiseOperator(ctx.bitwise_operator().getText());
        System.out.println(num);
        System.out.println(repNum);
        System.out.println(ctx.bitwise_operator().getText());
        return new Loop(shiftOrShiftGroupOrMergeName, entityOrEntityGroupName, bO, num, repNum);
    }

    @Override
    public IfThenElse visitIfthenelse(SchedulerParser.IfthenelseContext ctx) {
        Cond cond = visitCond(ctx.cond());
        List<Transformation> thenTransformations = new ArrayList<>();
        List<Transformation> elseTransformations = new ArrayList<>();

        for (SchedulerParser.TransformationContext e : ctx.thenblock.transformation()) {
            if (e.apply() != null) {
                thenTransformations.add(visitApply(e.apply()));
            } else if (e.merge() != null) {
                thenTransformations.add(visitMerge(e.merge()));
            } else if (e.loop() != null) {
                thenTransformations.add(visitLoop(e.loop()));
            } else if (e.ifthenelse() != null) {
                thenTransformations.add(visitIfthenelse(e.ifthenelse()));
            }
        }

        for (SchedulerParser.TransformationContext e : ctx.elseblock.transformation()) {
            if (e.apply() != null) {
                elseTransformations.add(visitApply(e.apply()));
            } else if (e.merge() != null) {
                elseTransformations.add(visitMerge(e.merge()));
            } else if (e.loop() != null) {
                elseTransformations.add(visitLoop(e.loop()));
            } else if (e.ifthenelse() != null) {
                elseTransformations.add(visitIfthenelse(e.ifthenelse()));
            }
        }
        return new IfThenElse(cond, thenTransformations, elseTransformations);
    }

    @Override
    public Cond visitCond(SchedulerParser.CondContext ctx) {
        LogicalOperator logicalOperator = getLogicalOperator(ctx.logical_operator().getText());
        String shiftOrShiftGroupName1 = ctx.name(0).getText();
        String shiftOrShiftGroupName2 = ctx.name(1).getText();
        return new Cond(logicalOperator, shiftOrShiftGroupName1, shiftOrShiftGroupName2);
    }

    // todo: fix this so that the lexer returns bitwise/logical operators without trailing whitespace
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

    private void addNestedTransformations(Map<String, List<Transformation>> transformationMap,
                                          List<Transformation> transformations) {
        for (Transformation t : transformations) {
            if (t.getClass().equals(Merge.class)){
                transformationMap.get(Transformation.MERGE).add(t);
            } else if (t.getClass().equals(Apply.class)){
                transformationMap.get(Transformation.APPLY).add(t);
            } else if (t.getClass().equals(Loop.class)) {
                transformationMap.get(Transformation.LOOP).add(t);
            } else if (t.getClass().equals(IfThenElse.class)) {
                transformationMap.get(Transformation.IF_THEN_ELSE).add(t);
            }
        }
    }
}
