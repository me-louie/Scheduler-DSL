package ast;

import ast.transformation.*;
import validate.DuplicateNameException;
import validate.ProgramValidationException;

public interface SchedulerVisitor<T> {

    T visit(Program p) throws ProgramValidationException;
    T visit(Header h) throws ProgramValidationException;
    T visit(Entity e) throws ProgramValidationException;
    T visit(EntityGroup eg) throws ProgramValidationException;
    T visit(Shift s) throws ProgramValidationException;
    T visit(ShiftGroup sg) throws ProgramValidationException;
    T visit(Apply a) throws ProgramValidationException;
    T visit(Merge m) throws ProgramValidationException;
    T visit(Loop l) throws ProgramValidationException;
    T visit(LogicalOperator lo) throws ProgramValidationException; // might not actually need these
    T visit(BitwiseOperator bo) throws ProgramValidationException;

}
