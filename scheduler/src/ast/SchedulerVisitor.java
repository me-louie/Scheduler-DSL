package ast;

public interface SchedulerVisitor<T> {

    T visit(Program p);
    T visit(Header h);
    T visit(Entity e);
    T visit(EntityGroup eg);
    T visit(Shift s);
    T visit(ShiftGroup sg);
    T visit(Apply a);
    T visit(Merge m);
    T visit(Loop l);
    T visit(LogicalOR lo); // might not actually need these
    T visit(LogicalAND la);
    T visit(LogicalXOR lx);
    T visit(BitwiseLeftShift bls);
    T visit(BitwiseRightShift brs);

}
