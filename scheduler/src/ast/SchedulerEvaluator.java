package ast;

import evaluate.ScheduledEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulerEvaluator implements SchedulerVisitor<Void> {

    // map of entity name to a list of all their scheduled events
    Map<String, List<ScheduledEvent>> scheduleMap = new HashMap<>();
    Program program;

    @Override
    public Void visit(Program p) {
        program = p;
        p.getHeader().accept(this);
        p.getEntity().forEach(e -> e.accept(this));
        p.getEntityGroup().forEach(eg -> eg.accept(this));
        p.getShifts().forEach(s -> s.accept(this));
        p.getShiftGroups().forEach(sg -> sg.accept(this));
        p.getTransformations().forEach(t -> t.accept(this));
        return null;
    }

    @Override
    public Void visit(Header h) {
        // in each visit do
        //  - basic validation (use global program to do necessary checks)
        //  - add an entry to scheduleMap if is an apply or loop
        //  - create a new ShiftGroup if is a merge
        return null;
    }

    @Override
    public Void visit(Entity e) {
        return null;
    }

    @Override
    public Void visit(EntityGroup eg) {
        return null;
    }

    @Override
    public Void visit(Shift s) {
        return null;
    }

    @Override
    public Void visit(ShiftGroup sg) {
        return null;
    }

    @Override
    public Void visit(Apply a) {
        return null;
    }

    @Override
    public Void visit(Merge m) {
        return null;
    }

    @Override
    public Void visit(Loop l) {
        return null;
    }

    @Override
    public Void visit(LogicalOR lo) {
        return null;
    }

    @Override
    public Void visit(LogicalAND la) {
        return null;
    }

    @Override
    public Void visit(LogicalXOR lx) {
        return null;
    }

    @Override
    public Void visit(BitwiseLeftShift bls) {
        return null;
    }

    @Override
    public Void visit(BitwiseRightShift brs) {
        return null;
    }
}
