package ast;

import ast.math.Expression;
import ast.math.Variable;
import ast.transformation.*;
import validate.ProgramValidationException;

public interface SchedulerVisitor<T> {

    T visit(Program p) throws ProgramValidationException;
    T visit(Entity e) throws ProgramValidationException;
    T visit(EntityGroup eg) throws ProgramValidationException;
    T visit(Shift s) throws ProgramValidationException;
    T visit(ShiftGroup sg) throws ProgramValidationException;
    T visit(Apply a) throws ProgramValidationException;
    T visit(Merge m) throws ProgramValidationException;
    T visit(Loop l) throws ProgramValidationException;
    T visit(IfThenElse i) throws ProgramValidationException;
    T visit(Cond c) throws ProgramValidationException;
    T visit(Expression f) throws ProgramValidationException;
    T visit(Variable v) throws ProgramValidationException;
}
