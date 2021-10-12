package ast.transformation;

import ast.SchedulerVisitor;
import validate.ProgramValidationException;

import java.util.List;

public class IfThenElse extends Transformation {
    private final Cond cond;
    private final List<Transformation> thenTransformations;
    private final List<Transformation> elseTransformations;

    public IfThenElse(Cond cond, List<Transformation> thenTransformations,
                      List<Transformation> elseTransformations) {
        this.cond = cond;
        this.thenTransformations = thenTransformations;
        this.elseTransformations = elseTransformations;
    }

    public Cond getCond() {
        return cond;
    }

    public List<Transformation> getThenTransformations() {
        return thenTransformations;
    }

    public List<Transformation> getElseTransformations() {
        return elseTransformations;
    }

    @Override
    public <T> T accept(SchedulerVisitor<T> v) throws ProgramValidationException {
        return v.visit(this);
    }

    @Override
    public String getName() {
        return null;
    }
}
