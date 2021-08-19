package model;

import app.Utils;

import java.util.ArrayList;

public class Expression {
    public String origin;
    public ArrayList<PolishRec> rec;
    Tristate value;

    public Expression(String origin) {
        this.origin = origin;
        this.rec = Utils.PolishNotation(origin);
        this.value = Tristate.UNDEF;
    }
}
