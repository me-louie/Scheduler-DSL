package parser;

import ast.*;
import ast.math.Expression;
import ast.math.MathOperation;
import ast.math.Variable;
import ast.transformation.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParseToASTVisitor extends AbstractParseTreeVisitor<Node> implements SchedulerParserVisitor<Node> {
    @Override
    public Program visitProgram(SchedulerParser.ProgramContext ctx) {
        Map<String, Variable> variableMap = new SchedulerMap<>();
        Map<String, Expression> expressionMap = new SchedulerMap<>();
        Map<String, Entity> entityMap = new SchedulerMap<>();
        Map<String, EntityGroup> entityGroupMap = new SchedulerMap<>();
        Map<String, Shift> shiftMap = new SchedulerMap<>();
        Map<String, ShiftGroup> shiftGroupMap = new SchedulerMap<>();
        Map<String, List<Transformation>> transformationMap = new SchedulerMap<>() {{
            put(Transformation.APPLY, new ArrayList<>());
            put(Transformation.MERGE, new ArrayList<>());
            put(Transformation.LOOP, new ArrayList<>());
            put(Transformation.IF_THEN_ELSE, new ArrayList<>());
        }};

        for (SchedulerParser.EntityContext e1 : ctx.entity()) {
            Entity entity = this.visitEntity(e1);
            entityMap.put(entity.getName(), entity);
        }

        for (SchedulerParser.Entity_groupContext e : ctx.entity_group()) {
            EntityGroup entityGroup = this.visitEntity_group(e);
            entityGroupMap.put(entityGroup.getName(), entityGroup);
        }

        for (SchedulerParser.ShiftContext s : ctx.shift()) {
            Shift shift = this.visitShift(s);
            shiftMap.put(shift.getName(), shift);
        }



        for (SchedulerParser.TransformationContext e : ctx.transformation()) {
            if(e.shift_group() != null){
                ShiftGroup shiftGroup = this.visitShift_group(e.shift_group());
                shiftGroupMap.put(shiftGroup.getName(), shiftGroup);
            }
            if (e.apply() != null) {
                Apply apply = this.visitApply(e.apply());
                transformationMap.get(Transformation.APPLY).add(apply);
            } else if (e.merge() != null) {
                Merge merge = this.visitMerge(e.merge());
                transformationMap.get(Transformation.MERGE).add(merge);
            } else if (e.loop() != null) {
                Loop loop = this.visitLoop(e.loop());
                transformationMap.get(Transformation.LOOP).add(loop);
            } else if (e.ifthenelse() != null) {
                IfThenElse ifThenElse = this.visitIfthenelse(e.ifthenelse());
                transformationMap.get(Transformation.IF_THEN_ELSE).add(ifThenElse);
            } else if (e.expression() != null) {
                Expression expression = this.visitExpression(e.expression());
                expressionMap.put(expression.getName(), expression);
            } else if (e.variable() != null) {
                Variable var = this.visitVariable(e.variable());
                variableMap.put(var.getName(), var);
            }
        }
        Program.setInstance(entityMap, entityGroupMap, shiftMap, shiftGroupMap, transformationMap,
                variableMap, expressionMap);
        return Program.getInstance();
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
        String begin = ctx.DATE(0).getText() + ctx.TIME(0).getText();
        String end = ctx.DATE(1).getText() + ctx.TIME(1).getText();
        String description = ctx.DESCRIPTION() != null ? ctx.DESCRIPTION().getText() : "";

        return new Shift(name, begin, end, description);
    }

    @Override
    public ShiftGroup visitShift_group(SchedulerParser.Shift_groupContext ctx) {
        String name = ctx.name(0).getText();
        List<String> sList = new ArrayList<>();

        for (int i = 1; i < ctx.name().size(); i++) {
            sList.add((ctx.name(i).getText()));
        }
        return new ShiftGroup(name, sList);
    }

    @Override
    public Node visitSet_operator(SchedulerParser.Set_operatorContext ctx) {
        return null;
    }

    @Override
    public Node visitOffset_operator(SchedulerParser.Offset_operatorContext ctx) {
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
    public Variable visitVariable(SchedulerParser.VariableContext ctx) {
        String varName = ctx.name().getText();
        if(isInt(varName)){
           throw new RuntimeException("Variable name \"" + varName + "\" must have a letter");
        }
        String varName2 = null;
        int num = 0;
        try {
            if (isInt(ctx.VARORNUM().getText())) {
                num = Integer.parseInt(ctx.VARORNUM().getText());
            } else {
                varName2 = ctx.VARORNUM().getText();
                if (varName.equals(varName2)) {
                    throw new RuntimeException(varName + " can't be used as variable name");
                }
            }
        } catch (NullPointerException e) {
            // if user declares but doesn't assign a value this will null pointer, in that case we automatically assign the value of 0
        }
        return new Variable(varName, num, varName2);
    }

    public boolean isInt(String str) {


        try {
            Integer.parseInt(str);
            return true;  // String is an Integer
        } catch (NumberFormatException e) {
            if (str.matches("[0-9]*[a-zA-Z]+")) {
                return false; // String is not an Integer
            }else if(str.matches("-?[0-9]+")){
                throw e;
            }else{
                return false;
            }
        }
    }

    @Override
    public Node visitTimeShiftUnits(SchedulerParser.TimeShiftUnitsContext ctx) {
        return null;
    }

    @Override
    public Apply visitApply(SchedulerParser.ApplyContext ctx) {
        String shiftOrShiftGroupOrMergeName = ctx.name(0).getText();
        String entityOrEntityGroupName = ctx.name(1).getText();
        int offsetAmount = 0;
        String varOrExpressionName = null;
        TimeUnit timeUnit = null;
        int repeatAmount = 1;
        if (ctx.VARORNUM() != null) {
            if (isInt(ctx.VARORNUM().getText())) {
                offsetAmount = Integer.parseInt(ctx.VARORNUM().getText());
            } else {
                varOrExpressionName = ctx.VARORNUM().getText();
            }
        }
        OffsetOperator offsetOperator = null;
        if (ctx.offset_operator() != null) {
            offsetOperator = getOffsetOperator(ctx.offset_operator().getText());
        }
        if (ctx.timeShiftUnits() != null) {
            timeUnit = getTimeShiftUnit(ctx.timeShiftUnits().getText());
        }
        if (ctx.NUM() != null) {
            repeatAmount = Integer.parseInt(ctx.NUM().getText());
        }
        return new Apply(shiftOrShiftGroupOrMergeName, entityOrEntityGroupName, offsetAmount, offsetOperator,
                timeUnit, varOrExpressionName, repeatAmount);
    }

    @Override
    public Expression visitExpression(SchedulerParser.ExpressionContext ctx) {
        String name = ctx.name().getText();
        Integer value1 = null;
        Integer value2 = null;
        String varOrExpression1 = null;
        String varOrExpression2 = null;
        if (isInt(ctx.VARORNUM(0).getText())) {
            value1 = Integer.parseInt(ctx.VARORNUM(0).getText());
        } else {
            varOrExpression1 = ctx.VARORNUM(0).getText();
            if (name.equals(varOrExpression1)) {
                throw new RuntimeException(name + " can't be used as variable name");
            }
        }

        if (isInt(ctx.VARORNUM(1).getText())) {
            value2 = Integer.parseInt(ctx.VARORNUM(1).getText());
        } else {
            varOrExpression2 = ctx.VARORNUM(1).getText();
            if (name.equals(varOrExpression2)) {
                throw new RuntimeException(name + " can't be used as variable name");
            }
        }
        MathOperation mathOP = getMathOperator(ctx.MATH().getText());

        return new Expression(name, varOrExpression1, varOrExpression2, mathOP, value1, value2);
    }

    @Override
    public Merge visitMerge(SchedulerParser.MergeContext ctx) {
        String name = ctx.name(0).getText();
        String shiftOrShiftGroup1 = ctx.name(1).getText();
        String shiftOrShiftGroupOrMerge = ctx.name(2).getText();
        SetOperator setOperator = getSetOperator(ctx.set_operator().getText());
        return new Merge(name, setOperator, shiftOrShiftGroup1, shiftOrShiftGroupOrMerge);
    }

    @Override
    public Loop visitLoop(SchedulerParser.LoopContext ctx) {
        String shiftOrShiftGroupOrMergeName = ctx.name(0).getText();
        String entityOrEntityGroupName = ctx.name(1).getText();
        int offsetAmount = 0;
        String varOrExpression = null;
        int repeatAmount = 1;
        if (isInt(ctx.VARORNUM().getText())) {
            offsetAmount = Integer.parseInt(ctx.VARORNUM().getText());
        } else {
            varOrExpression = ctx.VARORNUM().getText();
        }
        if (ctx.NUM() != null) {
            repeatAmount = Integer.parseInt(ctx.NUM().getText());
        }
        OffsetOperator offsetOperator = getOffsetOperator(ctx.offset_operator().getText());

        TimeUnit timeUnit = null;
        if (ctx.timeShiftUnits() != null) {
            timeUnit = getTimeShiftUnit(ctx.timeShiftUnits().getText());
        }
        return new Loop(shiftOrShiftGroupOrMergeName, entityOrEntityGroupName, offsetOperator, offsetAmount, repeatAmount, varOrExpression, timeUnit);
    }

    @Override
    public IfThenElse visitIfthenelse(SchedulerParser.IfthenelseContext ctx) {
        Cond cond = visitCond(ctx.cond());
        List<Transformation> thenTransformations = new ArrayList<>();
        List<Transformation> elseTransformations = new ArrayList<>();

        visitNestedTransformations(ctx.thenblock.transformation(), thenTransformations);
        visitNestedTransformations(ctx.elseblock.transformation(), elseTransformations);

        return new IfThenElse(cond, thenTransformations, elseTransformations);
    }

    @Override
    public Cond visitCond(SchedulerParser.CondContext ctx) {
        SetOperator setOperator = getSetOperator(ctx.set_operator().getText());
        String shiftGroupOrMergeGroupName1 = ctx.name(0).getText();
        String shiftGroupOrMergeGroupName2 = ctx.name(1).getText();
        return new Cond(setOperator, shiftGroupOrMergeGroupName1, shiftGroupOrMergeGroupName2);
    }

    private MathOperation getMathOperator(String mathOperator) {
        return switch (mathOperator.trim()) {
            case "+" -> MathOperation.PLUS;
            case "-" -> MathOperation.MINUS;
            case "*" -> MathOperation.MULTIPLY;
            case "/" -> MathOperation.DIVIDE;
            case "^" -> MathOperation.POWER;
            default -> throw new RuntimeException("Unrecognized time unit");
        };
    }

    private TimeUnit getTimeShiftUnit(String timeShiftUnit) {
        return switch (timeShiftUnit.trim()) {
            case "HOURS" -> TimeUnit.HOURS;
            case "DAYS" -> TimeUnit.DAYS;
            case "WEEKS" -> TimeUnit.WEEKS;
            case "MONTHS" -> TimeUnit.MONTHS;
            case "YEARS" -> TimeUnit.YEARS;
            default -> throw new RuntimeException("Unrecognized time unit");
        };
    }

    private OffsetOperator getOffsetOperator(String operator) {
        return switch (operator.trim()) {
            case "<<" -> OffsetOperator.LEFTSHIFT;
            case ">>" -> OffsetOperator.RIGHTSHIFT;
            default -> throw new RuntimeException("Unrecognized offset operator");
        };
    }

    private SetOperator getSetOperator(String operator) {
        return switch (operator.trim()) {
            case "AND" -> SetOperator.AND;
            case "OR" -> SetOperator.OR;
            case "XOR" -> SetOperator.XOR;
            case "EXCEPT" -> SetOperator.EXCEPT;
            default -> throw new RuntimeException("Unrecognized set operator");
        };
    }

    private void visitNestedTransformations(List<SchedulerParser.TransformationContext> ctxTransformations,
                                            List<Transformation> transformations) {
        for (SchedulerParser.TransformationContext e : ctxTransformations) {
            if (e.apply() != null) {
                transformations.add(visitApply(e.apply()));
            } else if (e.merge() != null) {
                transformations.add(visitMerge(e.merge()));
            } else if (e.loop() != null) {
                transformations.add(visitLoop(e.loop()));
            } else if (e.ifthenelse() != null) {
                transformations.add(visitIfthenelse(e.ifthenelse()));
            }
        }
    }
}
