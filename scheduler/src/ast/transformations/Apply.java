package ast.transformations;

import ast.Entity;
import ast.EntityGroup;
import ast.Shift;
import ast.Shit_group;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Apply extends Transformations{



    private Shit_group sg;
    private Shift s;
    private Merge m;
    private Entity e;
    private EntityDATEGroup eG;


    public Apply(Shit_group sg, Shift s, Merge m, Entity e, EntityGroup eG) {
        this.sg = sg;
        this.s = s;
        this.m = m;
        this.e = e;
        this.eG = eG;
    }

    @Override
    public void evaluate(PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
        
    }
}
