package model;

import java.util.ArrayList;

public class Fact {
    private String name;
    private boolean defined;

    private Tristate state;


    public Fact() {
        state = Tristate.UNDEF;
        defined = false;
        ArrayList<ArrayList<PolishRec>> definers;
    }

    public Fact(Tristate state) {
        this.state = state;
    }

    public void define(Tristate state) {
        this.state = state;
        defined = true;
    }

    @Override
    public String toString() {
        String stringValue;

        if (!defined)
            stringValue = "undefined";
        else
            stringValue = state.toString();

        return String.format(
                "%s - %s",
                name,
                stringValue);
    }
}
