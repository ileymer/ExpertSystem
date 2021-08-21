package model;

import app.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Expression {
    public String origin;
    public ArrayList<PolishRec> rec;
    public Tristate value;
    public boolean visited;

    public Expression(String origin) {
        this.origin = origin;
        this.visited = false;
        this.rec = Utils.PolishNotation(origin);
        this.value = Tristate.UNDEF;
    }


}
